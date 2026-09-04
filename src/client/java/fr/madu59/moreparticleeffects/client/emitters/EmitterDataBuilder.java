package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import org.joml.Vector3f;

import net.minecraft.resources.Identifier;

public class EmitterDataBuilder {
    private static final float DEFAULT_PROBABILITY = 1.0f;
    
    protected Identifier id = null;
    protected Predicate<EmitterContext> predicate = (ctx) -> true;
    protected EmitterShape shape = EmitterShape.CUBE;
    protected Vector3f size = new Vector3f(1, 1, 1);
    protected float probability = DEFAULT_PROBABILITY;

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

    public EmitterDataBuilder setProbability(float probability) {
        this.probability = probability;
        return this;
    }

    public EmitterDataBuilder resetProbability() {
        this.probability = DEFAULT_PROBABILITY;
        return this;
    }

    public EmitterData build() {
        return new EmitterData(this);
    }
}
