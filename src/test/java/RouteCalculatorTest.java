import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RouteCalculatorTest extends TestCase
{   RouteCalculator calculator;
    StationIndex stationIndex;


//       Left st----Central st-----Right st---------
//                      |                            |
//                Eastern st---Western st           |
//                              |                    |
//                         Northern st---South st---Last
//                                        |
//                                      Lowest st
    @Override
    protected void setUp() throws Exception
    {
        stationIndex = new StationIndex();
        calculator = new RouteCalculator(stationIndex);

        List<Line> lines = new ArrayList<>() {{
            add(new Line(1, "lineOne"));
            add(new Line(2, "lineTwo"));
            add(new Line(3, "lineThree"));
            add(new Line(4, "lineFour"));
        }};

        for (Line line : lines) {
            stationIndex.addLine(line);
        }

        List<Station> stations = new ArrayList<>() {{
            add(new Station("Left", stationIndex.getLine(1)));
            add(new Station("Central", stationIndex.getLine(1)));
            add(new Station("Right", stationIndex.getLine(1)));
            add(new Station("Eastern", stationIndex.getLine(2)));
            add(new Station("Western", stationIndex.getLine(2)));
            add(new Station("Northern", stationIndex.getLine(3)));
            add(new Station("South", stationIndex.getLine(3)));
            add(new Station("Last", stationIndex.getLine(3)));
            add(new Station("Lowest", stationIndex.getLine(4)));
        }};
        for(Station station: stations)
        {
            if(station.getLine().equals(stationIndex.getLine(1))){
                stationIndex.getLine(1).addStation(station);
            }
            else if(station.getLine().equals(stationIndex.getLine(2))){
                stationIndex.getLine(2).addStation(station);
            }
            else if(station.getLine().equals(stationIndex.getLine(3))){
                stationIndex.getLine(3).addStation(station);
            }
            else stationIndex.getLine(4).addStation(station);

        }
        for(Station station : stations)
    {
        stationIndex.addStation(station);
    }

        List<Station> con1 = getList("Central, Eastern");
        List<Station> con2 = getList("Western, Northern");
        List<Station> con3 = getList("South, Lowest");
        stationIndex.addConnection(con1);
        stationIndex.addConnection(con2);
        stationIndex.addConnection(con3);

        }

    public void testCalculateDirection()
    {
        double actualTime = RouteCalculator.calculateDuration(getList("Left, Central, Right"));
        double expectedTime = 5;
        assertEquals(expectedTime, actualTime);
    }
    public void testGetRouteOnTheLine()
    {
        List<Station> actual = calculator.getShortestRoute(stationIndex.getStation("Northern"),
                stationIndex.getStation("Last"));
        List<Station> expected = getList("Northern, South, Last");
        assertEquals(expected, actual);


    }
    public void testGetRouteWithOneConnection()
    {
        List<Station> actual = calculator.getShortestRoute(stationIndex.getStation("Central"),
                stationIndex.getStation("Western"));
        List<Station> expected = getList("Central, Eastern, Western");
        assertEquals(expected, actual);
    }
    public void testGetRouteWithTwoConnections()
    {
        List<Station> actual = calculator.getShortestRoute(stationIndex.getStation("Western"),
                stationIndex.getStation("Lowest"));
        List<Station> expected = getList("Western, Northern, South, Lowest");
        assertEquals(expected, actual);
    }
    private List<Station> getList(String names) {
        String[] stations = names.split(", ");
        return Arrays.stream(stations)
                .map(s -> stationIndex.getStation(s.trim()))
                .collect(Collectors.toList());
    }

    @Override
    protected void tearDown() throws Exception
    {

    }
}
