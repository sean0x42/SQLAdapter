package io.seanbailey;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<String> errors;

//    /**
//     * @return a new SQL Query chain.
//     */
//    public static QueryChain newQuery () {
//        return new QueryChain(Model.class.asSubclass(Model.class));
//    }

    public void beforeValidate () {

    }

    public void validate () {
        beforeValidate();
        errors = new ArrayList<>();
        afterValidate();
    }

    public void afterValidate() {

    }

    public void beforeSave () {

    }

    public void save () {
        beforeSave();
        validate();
        // Save
        afterSave();
    }

    public void afterSave () {

    }

}
