package com.ebicep.warlords.classes.paladin.specs.crusader;

import com.ebicep.warlords.classes.abilties.*;
import com.ebicep.warlords.classes.paladin.AbstractPaladin;

public class Crusader extends AbstractPaladin {

    public Crusader() {
        super(6850, 305, 20,
                new Strike("Crusader's Strike", -326, -441, 0, 90, 25, 175),
                new Consecrate(-144, -194.4f, 50, 15, 200, 15),
                new LightInfusion(15.66f),
                new HolyRadiance(19.57f, 20, 15, 175),
                new InspiringPresence());
    }

}
