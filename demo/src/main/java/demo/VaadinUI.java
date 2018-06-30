package demo;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import io.github.atistrcsn.sectormapper.SectorView;
import io.github.atistrcsn.sectormapper.SectorMapper;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL0;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL1;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL2;
import static io.github.atistrcsn.sectormapper.SectorLevel.LEVEL3;

@Theme("valo")
@SpringUI
public class VaadinUI extends UI {

    private SectorMapper sectorMapper;

    public VaadinUI(SectorMapper sectorMapper) {
        this.sectorMapper = sectorMapper;
    }

    @Override
    protected void init(VaadinRequest request) {
        sectorMapper.init();

        Arrays.asList(
                new SectorView(3, 116, 698, LEVEL0, "3. SZEK."),
                new SectorView(4, 677, 1431, LEVEL0, "4. SZEK."),
                new SectorView(5, 677, 1003, LEVEL0, "5. SZEK."),
                new SectorView(6, 677, 649, LEVEL0, "6. SZEK."),
                new SectorView(7, 2075, 890, LEVEL0, "7. SZEK."),
                new SectorView(8, 1796, 1326, LEVEL0, "8. SZEK."),
                new SectorView(9, 1520, 890, LEVEL0, "9. SZEK."),
                new SectorView(11, 3544, 890, LEVEL0, "11. SZEK."),
                new SectorView(12, 2689, 890, LEVEL0, "12. SZEK."),
                new SectorView(13, 3112, 1800, LEVEL0, "13. SZEK.")
        ).forEach(sector -> {
            sectorMapper.addSector(sector);
            sector.updateData(160, 13, Arrays.asList(LEVEL0, LEVEL1, LEVEL2, LEVEL3)
                                             .get(ThreadLocalRandom.current().nextInt(0, 3 + 1))
            );
        });
    }
}
