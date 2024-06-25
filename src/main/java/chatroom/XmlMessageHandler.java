package chatroom;

import chatroom.TextMessages.TextMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class XmlMessageHandler {

    private static final String FILE_PATH = "messages.xml";

    public static void writeMessageToXml(TextMessages message) {
        try {
            File file = new File(FILE_PATH);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document;

            if (file.exists()) {
                document = documentBuilder.parse(file);
                document.getDocumentElement().normalize();
            } else {
                document = documentBuilder.newDocument();
                Element root = document.createElement("messages");
                document.appendChild(root);
            }

            Element root = document.getDocumentElement();

            Element msg = document.createElement("message");

            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(String.valueOf(message.getId())));
            msg.appendChild(id);

            Element userId = document.createElement("userId");
            userId.appendChild(document.createTextNode(String.valueOf(message.getSenderId())));
            msg.appendChild(userId);

            Element text = document.createElement("text");
            text.appendChild(document.createTextNode(message.getMessage()));
            msg.appendChild(text);

            Element timestamp = document.createElement("timestamp");
            timestamp.appendChild(document.createTextNode(message.getTimestamp().toString()));
            msg.appendChild(timestamp);

            root.appendChild(msg);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);

            System.out.println("Dữ liệu đã được lưu vào file XML thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TextMessages> readMessagesFromXml() {
        List<TextMessages> messages = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return messages;
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("message");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    int userId = Integer.parseInt(element.getElementsByTagName("userId").item(0).getTextContent());
                    String text = element.getElementsByTagName("text").item(0).getTextContent();
                    Timestamp timestamp = Timestamp.valueOf(element.getElementsByTagName("timestamp").item(0).getTextContent());

                    messages.add(new TextMessages(id, userId, timestamp, text));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }
}
