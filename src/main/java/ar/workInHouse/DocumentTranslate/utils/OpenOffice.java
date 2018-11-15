package ar.workInHouse.DocumentTranslate.utils;

import java.io.File;
import java.io.IOException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import net.sf.jooreports.templates.DocumentTemplateException;

public class OpenOffice {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OpenOffice.class);
	static private OpenOffice openOffice = null;
	private int PORT = 8100;

	public static OpenOffice getInstance() throws IOException, DocumentTemplateException {
		if (openOffice == null) {
			openOffice = new OpenOffice();
		}
		return openOffice;
	}

	private OpenOffice() throws IOException, DocumentTemplateException {}

	public void open(File input, File output) throws IOException, DocumentTemplateException, Exception {
		File inputFile = input;
		File outputFile = output;
		DocumentFormat inFormat = null;
		DocumentFormat outFormat = null;
		String extensionInput;
		String extensionOutput;

		OpenOfficeConnection connection = null;

		try {
			connection = new SocketOpenOfficeConnection(PORT);
		} catch (Exception e) {
			//LOG.error(e.getMessage(), e);
			throw new Exception("A. new SocketOpenOfficeConnection: " + e.getClass() + ": " + e.toString());
		}
		
        DocumentConverter converter = null;
        try {  
            converter = new OpenOfficeDocumentConverter(connection);
        } catch (Exception e) {
        	//LOG.error(e.getMessage(),e);
            connection.disconnect();
            throw new Exception("C. OpenOfficeDocumentConverter(connection): " + e.getClass() + ": " + e.toString());
        }		

        try {
            extensionInput = input.getName().substring( input.getName().lastIndexOf('.') + 1 );
            extensionOutput = output.getName().substring( output.getName().lastIndexOf('.') + 1 );

            if (extensionInput.equalsIgnoreCase("odt")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "application/vnd.oasis.opendocument.text", "odt"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "writer8");    
            } else if (extensionInput.equalsIgnoreCase("doc")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "application/msword", "doc"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "MS Word 97");    
            } else if (extensionInput.equalsIgnoreCase("rtf")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "text/rtf", "rtf"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "Rich Text Format");    
            } else if (extensionInput.equalsIgnoreCase("txt")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "text/txt", "txt"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "Text (encoded)");  
              
                // Setea que la configuracion de entrada es de Latinoamérica.  
                inFormat.setImportOption("FilterName", "Text (encoded)");
                inFormat.setImportOption("FilterOptions", "ISO_8859-1,CRLF");
            } else if (extensionInput.equalsIgnoreCase("pdf")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "application/pdf", "pdf"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");    
            }else if (extensionInput.equalsIgnoreCase("html")) {
                inFormat = new DocumentFormat("entrada", DocumentFamily.TEXT, "text/html", "html"); 
                inFormat.setExportFilter(DocumentFamily.TEXT, "HTML (StarWriter)");
            }

            if (extensionOutput.equalsIgnoreCase("odt")) {
                outFormat = new DocumentFormat("salida", DocumentFamily.TEXT, "application/vnd.oasis.opendocument.text", "odt"); 
                outFormat.setExportFilter(DocumentFamily.TEXT, "writer8");    
            } else if (extensionOutput.equalsIgnoreCase("doc")) {
                outFormat = new DocumentFormat("salida", DocumentFamily.TEXT, "application/msword", "doc"); 
                outFormat.setExportFilter(DocumentFamily.TEXT, "MS Word 97");    
            } else if (extensionOutput.equalsIgnoreCase("rtf")) {
                outFormat = new DocumentFormat("salida", DocumentFamily.TEXT, "text/rtf", "rtf"); 
                outFormat.setExportFilter(DocumentFamily.TEXT, "Rich Text Format");    
            } else if (extensionOutput.equalsIgnoreCase("txt")) {
                outFormat = new DocumentFormat("salida", DocumentFamily.TEXT, "text/TXT", "txt"); 
                outFormat.setExportFilter(DocumentFamily.TEXT, "Text (encoded)");    
            } else if (extensionOutput.equalsIgnoreCase("pdf")) {
                outFormat = new DocumentFormat("salida", DocumentFamily.TEXT, "application/pdf", "pdf"); 
                outFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");    
            }
        } catch (Exception e) {
        	//LOG.error(e.getMessage(),e);
            connection.disconnect();
            throw new Exception("D. Aplicar formato: " + e.getClass() + ": " + e.toString());
        }
        
        try {
            
            converter.convert(inputFile, inFormat, outputFile, outFormat);
            
        } catch (Exception e) {
        	//LOG.error(e.getMessage(),e);
            connection.disconnect();
            throw new Exception("No se pudo convertir de ." + extensionInput + " a ." + extensionOutput );
        }

        try {
            connection.disconnect();
        } catch (Exception e) {
        	//LOG.error(e.getMessage(),e);
            throw new Exception("F. connection.disconnect(): " + e.getClass() + ": " + e.toString());
        }        
	}

}
