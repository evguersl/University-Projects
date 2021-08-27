package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Interface who defines the Serde
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public interface Serde <T>
{
    /**
     *
     * @param t the object to serialze
     * @return it's serialization
     */
    String serialize (T t);

    /**
     *
     * @param string the string to deserialize
     * @return the corresponding object
     */
    T deserialize (String string) ;

    /**
     *
     * @param serialisation the serialization method
     * @param deserialisation the deserialization method
     * @param <T> the object to serialize/deserialize
     * @return a new Serde Object
     */
    static <T> Serde<T> of(Function<T,String> serialisation, Function<String,T> deserialisation)
    {
        return new SerdeObject<>(serialisation, deserialisation);
    }

    /**
     *
     * @param list all the values of an enum type
     * @param <T> the corresponding enum type
     * @return a new Serde Object capable of serializing and deserializing the enum type
     */
    static <T> Serde<T> oneOf(List<T> list)
    {
        Function<T,String> serialisation = i -> i==null ? "" : Integer.toString(list.indexOf(i));

        Function<String,T> deserialisation = i -> i.equals("") ? null : list.get(Integer.parseInt(i));

        return new SerdeObject<>(serialisation, deserialisation);
    }

    /**
     *
     * @param serde the serde to modify
     * @param separateur the separator character
     * @param <T> the corresponding type of the Serde
     * @return a new Serde Object
     */
    static <T> Serde<List<T>> listOf(Serde<T> serde, String separateur)
    {
        Function<List<T>,String> serialise = t ->
            {
                List<String> list =new ArrayList<>();
                t.forEach(f->list.add(serde.serialize(f)));
                return String.join(separateur,list);
            };

        Function<String, List<T>> deserialise = string ->
            {
                String[] a = string.split(Pattern.quote(separateur),-1);
                List<String> l = Arrays.asList(a);
                List<T> listeDeserialise = new ArrayList<>();
                if(string.isEmpty()){return listeDeserialise;}
                l.forEach(s -> listeDeserialise.add(serde.deserialize(s)));
                return listeDeserialise;
            };
        return new SerdeObject<>(serialise, deserialise);
    }

    /**
     *
     * @param serde the serde to modify
     * @param separateur the separator character
     * @param <T> the corresponding type of the Serde
     * @return a new Serde Object
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String separateur)
    {
        Serde<List<T>> listSerde = Serde.listOf(serde,separateur);
        Function<SortedBag<T>,String> serialise = sb->
            {
                List<T> elements = sb.toList();
                return listSerde.serialize(elements);

            };
        Function<String,SortedBag<T>> deserialise = s->
            {
                List<T> elements = listSerde.deserialize(s);
                return SortedBag.of(elements);

            };
        return new SerdeObject<>(serialise,deserialise);
    }

    /**
     * Our Serde Object class
     * @param <T> the type of the Object in the Serde
     */
    class SerdeObject<T> implements Serde<T>
    {
        private final Function<T,String> serialisation;
        private final Function<String,T> deserialisation;

        private SerdeObject (Function<T,String> serialisation, Function<String,T> deserialisation)
        {
            this.serialisation = serialisation;
            this.deserialisation = deserialisation;
        }

        @Override
        public String serialize(T o)
        {
            return serialisation.apply(o);
        }

        @Override
        public T deserialize(String string)
        {
            return deserialisation.apply(string);
        }
    }
}

