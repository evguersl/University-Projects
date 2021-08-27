package ch.epfl.tchu.gui;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Class who creates javaFx shape
 * @author Jean-Francois rocher (316766)
 * @author Evgueni Rousselot (320195)
 */

public abstract class CreateShape
{
    /**
     * cretate a JavaFx Rectangle with desired size and style classes
     * @param width the width of the rectangle
     * @param high the high of the rectangle
     * @param styleClass the list<String> corresponding to the styleClass
     * @return the Rectangle
     */
    public static Rectangle createRectangle(double width, double high, List<String> styleClass)
    {
        Rectangle rectangle = new Rectangle();
        rectangle.getStyleClass().addAll(styleClass);
        rectangle.setWidth(width);
        rectangle.setHeight(high);
        return rectangle;
    }

    /**
     * create a JavaFx Circle with the desired coordinates and radius
     * @param x the x coordiante
     * @param y the y coordinate
     * @param radius the radius of the desired circle
     * @return the circle
     */


    public static Circle createCircle (double x, double y, double radius)
    {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
        return circle;
    }

}
