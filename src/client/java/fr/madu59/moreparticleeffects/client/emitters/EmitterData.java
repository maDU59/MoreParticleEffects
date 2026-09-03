package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import net.minecraft.resources.Identifier;

public class EmitterData {
    private Identifier id;
    private Predicate<EmitterContext> predicate;

    public EmitterData(EmitterDataBuilder builder) {
        this.id = builder.id;
        this.predicate = builder.predicate;
    }

    public Identifier getId() {
        return id;
    }

    public Predicate<EmitterContext> getPredicate() {
        return predicate;
    }
}
