package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class PhlogiportModel extends DefaultedBlockGeoModel<PhlogiportBlockEntity> {
    public PhlogiportModel() {
        super(OccultEngineering.asResource("phlogiport"));
    }
}
