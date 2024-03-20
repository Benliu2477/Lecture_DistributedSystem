package be.kuleuven.foodrestservice.exceptions;

public class NoMealAvailableException extends RuntimeException {

    public NoMealAvailableException() {
        super("No meal available");
    }

}
