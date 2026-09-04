package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import org.joml.Vector3f;

import net.minecraft.resources.Identifier;

public class EmitterDataBuilder {
    protected Identifier id = null;
    protected Predicate<EmitterContext> predicate = (ctx) -> true;
    protected EmitterShape shape = EmitterShape.CUBE;
    protected Vector3f size = new Vector3f(0, 0, 0);

    public EmitterDataBuilder(Identifier id) {
        this.id = id;
    }

    public EmitterDataBuilder setPredicate(Predicate<EmitterContext> predicate) {
        this.predicate = predicate;
        return this;
    }

    public EmitterDataBuilder setShape(EmitterShape shape) {
        this.shape = shape;
        return this;
    }

    public EmitterDataBuilder setSize(Vector3f size) {
        this.size = size;
        return this;
    }

    public EmitterDataBuilder setSize(float size) {
        this.size = new Vector3f(size, size, size);
        return this;
    }

    public EmitterData build() {
        return new EmitterData(this);
    }
}
