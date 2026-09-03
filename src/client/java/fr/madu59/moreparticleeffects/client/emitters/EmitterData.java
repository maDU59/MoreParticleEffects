package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import net.minecraft.resources.Identifier;

public class EmitterData {
    private Identifier id;
    private Predicate<EmitterContext> predicate;

    public EmitterData(Identifier id, Predicate<EmitterContext> predicate) {
        this.id = id;
        this.predicate = predicate;
    }

    public Identifier getId() {
        return id;
    }

    public Predicate<EmitterContext> getPredicate() {
        return predicate;
    }
}
