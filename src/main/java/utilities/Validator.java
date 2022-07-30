package utilities;

import exceptions.UnvalidValueException;

import java.util.Arrays;
import java.util.List;

public class Validator {
    public static final List<String> VENDOR_OPTIONS_LIST = Arrays.asList("Siemens", "GE", "Philips");
    public static final List<String> POWER_OPTIONS_LIST = Arrays.asList("1.5T", "3.0T");
    public static final List<String> VNC_OPTIONS_LIST = Arrays.asList("true", "false");


    /**
     * @param type the given type to check
     * @param input the given input to  check
     * @throws UnvalidValueException if to input dosen't exists in the list of the type
     */
    public static void validateInput(Type type, String input) throws UnvalidValueException {
        switch (type) {
            case Vendor: {
                if (!VENDOR_OPTIONS_LIST.contains(input)) {
                    throw new UnvalidValueException(input, VENDOR_OPTIONS_LIST);
                }
                break;
            }
            case Power: {
                if (!POWER_OPTIONS_LIST.contains(input)) {
                    throw new UnvalidValueException(input, POWER_OPTIONS_LIST);
                }
                break;
            }
            case VNC: {
                if (!VNC_OPTIONS_LIST.contains(input)) {
                    throw new UnvalidValueException(input, VNC_OPTIONS_LIST);
                }
                break;
            }
        }


    }

}
