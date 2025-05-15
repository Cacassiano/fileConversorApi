package dev.cacassiano.conversor_de_arquivos.service;

public enum SuportedTypes {
    IMAGE_TYPES("gif", "jpg", "jpeg", "png"),
    FILE_TYPES("pdf","docx","odt", "doc");

    private final String[] types;
    private SuportedTypes(String... types) {
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }
}
