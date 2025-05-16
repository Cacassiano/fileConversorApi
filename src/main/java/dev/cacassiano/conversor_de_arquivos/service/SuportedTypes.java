package dev.cacassiano.conversor_de_arquivos.service;

import java.util.ArrayList;
import java.util.List;

public enum SuportedTypes {
    IMAGE_INPUT_TYPES("gif", "jpg", "jpeg", "png"),
    IMAGE_OUTPUT_TYPES("jpg", "jpeg", "png"),
    TEXT_INPUT_TYPES("pdf","docx","odt", "doc"),
    TEXT_OUTPUT_TYPES("pdf");

    private final String[] types;
    private SuportedTypes(String... types) {
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

    public String[] getAllOutputs(){
        List<String> outputs = new ArrayList<>();
        for ( String i : IMAGE_OUTPUT_TYPES.types) {
            outputs.add(i);
        }
        for(String i : TEXT_OUTPUT_TYPES.types){
            outputs.add(i);
        }

        return outputs.toArray(new String[1]);
    }

    public String[] getAllInputs() {
        List<String> inputs = new ArrayList<>();
        for (String i : IMAGE_INPUT_TYPES.types) {
            inputs.add(i);
        }
        for (String i : TEXT_INPUT_TYPES.types) {
            inputs.add(i);
        }

        return inputs.toArray(new String[1]);
    }
}
