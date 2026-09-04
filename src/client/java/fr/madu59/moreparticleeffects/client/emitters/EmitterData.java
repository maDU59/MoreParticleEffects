package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import org.joml.Vector3f;

import net.minecraft.resources.Identifier;

public class EmitterData {
    private Identifier id;
    private Predicate<EmitterContext> predicate;
    private EmitterShape shape;
    private Vector3f size;

    public EmitterData(EmitterDataBuilder builder) {
        this.id = builder.id;
        this.predicate = builder.predicate;
        this.shape = builder.shape;
        this.size = builder.size;
    }

    public Identifier getId() {
        return id;
    }

    public Predicate<EmitterContext> getPredicate() {
        return predicate;
    }

    public EmitterShape getShape() {
        return shape;
    }

    public Vector3f getSize() {
        return size;
    }
}
