package demo;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import io.github.atistrcsn.sectormapper.SectorMapper;
import io.github.atistrcsn.sectormapper.SectorView;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL0;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL1;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL2;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL3;

@Theme("valo")
@SpringUI
public class VaadinUI extends UI {

    private SectorMapper sectorMapper;
    private Logger logger = Logger.getLogger(VaadinUI.class.getSimpleName());

    public VaadinUI(SectorMapper sectorMapper) {
        this.sectorMapper = sectorMapper;
    }

    @Override
    protected void init(VaadinRequest request) {
        logger.log(Level.FINEST, "UI id: " + this.getId());
        sectorMapper.init();

        Arrays.asList(
                new SectorView(3, 79, 918, LEVEL0, "3. SZEK."),
                new SectorView(4, 642, 1394, LEVEL1, "4. SZEK."),
                new SectorView(5, 642, 973, LEVEL0, "5. SZEK."),
                new SectorView(6, 642, 609, LEVEL0, "6. SZEK."),
                new SectorView(7, 2039, 890, LEVEL2, "7. SZEK."),
                new SectorView(8, 1760, 1286, LEVEL0, "8. SZEK."),
                new SectorView(9, 1486, 890, LEVEL1, "9. SZEK."),
                new SectorView(11, 3506, 890, LEVEL0, "11. SZEK."),
                new SectorView(12, 2652, 890, LEVEL3, "12. SZEK."),
                new SectorView(13, 3076, 1770, LEVEL3, "13. SZEK.")
        ).forEach(sector -> {

            sectorMapper.addSector(sector);
            sector.updateData(160, 13, sector.getSectorLevel());
        });
    }
}
