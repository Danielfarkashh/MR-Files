package exceptions;

import java.util.List;

public class UnvalidValueException extends Exception {


    public UnvalidValueException(String unvalidValud, List<String> list) {
        super(String.format("value - {%s} not in the options - {%s}", unvalidValud, list.toString()));
    }
}
