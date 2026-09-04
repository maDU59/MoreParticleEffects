package fr.madu59.moreparticleeffects.client.emitters;

import org.joml.Vector3f;

public enum EmitterShape {
    SPHERE,
    CUBE,
    VOXELSHAPE;

    public static EmitterShape fromString(String name) {
        if (name == null || name.isEmpty()) return null;

        String normalized = name.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();

        try {
            return EmitterShape.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Vector3f getSizeFromIterable(Iterable<?> sizeIterable) {
        float sizeX = 0.0f;
        float sizeY = 0.0f;
        float sizeZ = 0.0f;
        int count = 0;

        for (Object value : sizeIterable) {
            if (value != null) {
                if(count == 0) sizeX = Float.valueOf(value.toString());
                else if(count == 1) sizeY = Float.valueOf(value.toString());
                else if(count == 2) sizeZ = Float.valueOf(value.toString());
                count++;
            }
        }

        if(count != 3) {
            System.err.println("Invalid size, expected 3 values but got " + count);
        }

        return count == 3 ? new Vector3f(sizeX, sizeY, sizeZ) : new Vector3f(0.0f, 0.0f, 0.0f);
    }
}
