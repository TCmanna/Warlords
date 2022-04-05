package com.ebicep.warlords.classes.mage.specs;

import com.ebicep.warlords.abilties.*;
import com.ebicep.warlords.classes.mage.AbstractMage;

public class Cryomancer extends AbstractMage {
    public Cryomancer() {
        super("Cryomancer", 5530, 305, 20, 20, 20,
                new FrostBolt(),
                new FreezingBreath(),
                new TimeWarp(),
                new ArcaneShield(),
                new IceBarrier());
    }
}
