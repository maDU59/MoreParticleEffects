package fr.madu59.moreparticleeffects.client.emitters;

import java.util.function.Predicate;

import net.minecraft.resources.Identifier;

public class EmitterDataBuilder {
    protected Identifier id = null;
    protected Predicate<EmitterContext> predicate = (ctx) -> true;

    public EmitterDataBuilder(Identifier id) {
        this.id = id;
    }

    public EmitterDataBuilder setPredicate(Predicate<EmitterContext> predicate) {
        this.predicate = predicate;
        return this;
    }

    public EmitterData build() {
        return new EmitterData(this);
    }
}
