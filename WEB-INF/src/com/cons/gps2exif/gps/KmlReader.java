package com.cons.gps2exif.gps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale.Category;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.cons.gps2exif.datamodel.MultiTrack;
import com.cons.gps2exif.datamodel.PlaceMark;
import com.cons.gps2exif.datamodel.Track;

public class KmlReader {

  public ArrayList<PlaceMark> readFromXML(InputStream is) throws XMLStreamException {
    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    XMLStreamReader reader = null;
    try {
      reader = inputFactory.createXMLStreamReader(is);
      return readPlaceMarks(reader);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  private ArrayList<PlaceMark> readPlaceMarks(XMLStreamReader reader) throws XMLStreamException {
    ArrayList<PlaceMark> placeMarks = new ArrayList<PlaceMark>();
    while (reader.hasNext()) {
      int eventType = reader.next();
      switch (eventType) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName().toLowerCase();
          if (elementName.equals("placemark")) {
            placeMarks.add(readPlaceMark(reader));
          }
          break;
        case XMLStreamReader.END_ELEMENT:
          break;
      }
    }
    return placeMarks;
  }

  private PlaceMark readPlaceMark(XMLStreamReader reader) throws XMLStreamException {
    PlaceMark placeMark = new PlaceMark();
    placeMark.setId(reader.getAttributeValue(null, "id"));

    while (reader.hasNext()) {
      int eventType = reader.next();
      switch (eventType) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();

          if (elementName.equals("name"))
            placeMark.setName(readCharacters(reader));
          else if (elementName.equals("description"))
            placeMark.setDescription(readCharacters(reader));
          else if (elementName.equals("styleUrl"))
            placeMark.setStyleurl(readCharacters(reader));
          else if (elementName.equals("ExtendedData"))
            placeMark.setTrackType(readExtendedData(reader));
          else if (elementName.equals("TimeStamp"))
            placeMark.setRecordDate(readTimeStamp(reader));
          else if (elementName.equals("Point")) {
            Float coord[] = readPoint(reader);
            placeMark.setLongitude(coord[0]);
            placeMark.setLatitude(coord[1]);
            placeMark.setAltitude(coord[2]);
          } else if (elementName.equals("MultiTrack")) {
            placeMark.setMultiTrack(readMultiTrack(reader));
          }
          break;
        case XMLStreamReader.END_ELEMENT:
          return placeMark;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }


  private Date readTimeStamp(XMLStreamReader reader) throws XMLStreamException {
    Date parsedDate = null;

    while (reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("when")) {

            GpsFileReader util = new GpsFileReader();
            parsedDate = util.getRecordDate(readCharacters(reader));

          }
          break;
        case XMLStreamReader.END_ELEMENT:
          return parsedDate;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }

  private Float[] readPoint(XMLStreamReader reader) throws XMLStreamException {
    Float[] coordinates = {0f, 0f, 0f};
    while (reader.hasNext()) {
      switch (reader.next()) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("coordinates")) {
            GpsFileReader util = new GpsFileReader();
            coordinates = util.getCoord(readCharacters(reader), ",");
          }
          break;
        case XMLStreamReader.END_ELEMENT:
          return coordinates;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }

  private String readExtendedData(XMLStreamReader reader) throws XMLStreamException {
    String dataType = "";

    while (reader.hasNext()) {
      int event1 = reader.next();

      switch (event1) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();
          if (elementName.equals("Data")) {

            while (reader.hasNext()) {
              int event2 = reader.next();

              if (event2 == XMLStreamReader.START_ELEMENT) {

                elementName = reader.getLocalName();
                if (elementName.equals("value")) {
                  dataType = readCharacters(reader);
                }
              } else if (event2 == XMLStreamReader.END_ELEMENT) {
                break;
              }
            }
          }
          break;
        case XMLStreamReader.END_ELEMENT:
          return dataType;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }


  private MultiTrack readMultiTrack(XMLStreamReader reader) throws XMLStreamException {
    MultiTrack multiTrack = new MultiTrack();
    ArrayList<Track> tracks = new ArrayList<Track>();
    multiTrack.setTracks(tracks);

    while (reader.hasNext()) {
      int eventType = reader.next();
      switch (eventType) {
        case XMLStreamReader.START_ELEMENT:
          String elementName = reader.getLocalName();

          if (elementName.equals("altitudeMode"))
            multiTrack.setAltitudeMode(readCharacters(reader));
          else if (elementName.equals("interpolate"))
            multiTrack.setInterpolate(readCharacters(reader));
          else if (elementName.equals("Track")) {
            multiTrack.getTracks().add(readTrack(reader));
          }
          break;

        case XMLStreamReader.END_ELEMENT:
          return multiTrack;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }

  private Track readTrack(XMLStreamReader reader) throws XMLStreamException {
    Track track = new Track();
    String elementName = "";

    while (reader.hasNext()) {
      int eventType = reader.next();

      if (eventType == XMLStreamReader.START_ELEMENT) {
        elementName = reader.getLocalName();

        if (elementName.equals("when")) {
          track.addWhens(readCharacters(reader));
        } else if (elementName.equals("coord")) {
          track.addCoord(readCharacters(reader));
        } else if (elementName.equals("SimpleArrayData")) {

          String attName = reader.getAttributeValue(0);

          if (attName.equals("speed")) {
            track.setSpeeds(readSimpleArrayData(reader));
          } else if (attName.equals("bearing")) {
            track.setBearings(readSimpleArrayData(reader));
          } else if (attName.equals("accuracy")) {
            track.setAccuracies(readSimpleArrayData(reader));
          }
        }
      } else if (elementName.equals("SimpleArrayData") && eventType == XMLStreamReader.END_ELEMENT) {
        return track;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }

  private ArrayList<String> readSimpleArrayData(XMLStreamReader reader) throws XMLStreamException {
    ArrayList<String> simpleData = new ArrayList<String>();
    String elementName = "";

    while (reader.hasNext()) {
      int eventType = reader.next();

      if (eventType == XMLStreamReader.START_ELEMENT) {
        elementName = reader.getLocalName();

        if (elementName.equals("value")) {
          simpleData.add(readCharacters(reader));
        }

      } else if (eventType == XMLStreamReader.END_ELEMENT) {
        return simpleData;
      }
    }
    throw new XMLStreamException("Premature end of file");
  }


  private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
    StringBuilder result = new StringBuilder();
    while (reader.hasNext()) {
      int eventType = reader.next();
      switch (eventType) {
        case XMLStreamReader.CHARACTERS:
        case XMLStreamReader.CDATA:
          result.append(reader.getText());
          break;
        case XMLStreamReader.END_ELEMENT:
          return result.toString();
      }
    }
    throw new XMLStreamException("Premature end of file");
  }

  private Category readCategory(XMLStreamReader reader) throws XMLStreamException {
    String characters = readCharacters(reader);
    try {
      return Category.valueOf(characters);
    } catch (IllegalArgumentException e) {
      throw new XMLStreamException("Invalid category " + characters);
    }
  }

  private int readInt(XMLStreamReader reader) throws XMLStreamException {
    String characters = readCharacters(reader);
    try {
      return Integer.valueOf(characters);
    } catch (NumberFormatException e) {
      throw new XMLStreamException("Invalid integer " + characters);
    }
  }
}
