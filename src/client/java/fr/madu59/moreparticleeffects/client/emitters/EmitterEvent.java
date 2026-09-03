package fr.madu59.moreparticleeffects.client.emitters;

import fr.madu59.moreparticleeffects.client.registry.EmitterRegistry;

public enum EmitterEvent {
    ON_BREAK,
    ON_PLACE,
    ON_FLATTEN,
    ON_TILL,
    ON_STRIP,
    ON_BREAK_BY_PLAYER,
    ON_STATE_CHANGE,
    ON_BLOCK_CHANGE;

    public void call(EmitterContext context) {
        for (EmitterData data : EmitterRegistry.getEmitters(this).values()) {
            if (data.getPredicate().test(context)) {
                System.out.println("Emitter " + data.getId() + " triggered for event " + this.name());
            }
        }

    }
}
