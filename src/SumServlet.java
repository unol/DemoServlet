import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SumServlet
 */ 
@WebServlet("/sumServlet")
public class SumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<Integer> fees = new ArrayList<Integer>();
	private int vehicleCount = 0;
	
    /**
     * Default constructor. 
     */
    public SumServlet() {
        // TODO Auto-generated constructor stub
    }

    
    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
   }

    
    public Float sum() {
    	float f=0;
    	
    	for(int i: fees) {
    		f+=i;
    	}
    	    	
    	System.out.println(round(f,2)/100);
		return round(f,2)/100;    
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String[] reqParaStr = request.getQueryString().split("=");
		String cmd = reqParaStr[0];
		String para = reqParaStr[1];
		
		if("fun".equals(cmd) && "sum".equals(para)) {		
			getApplication().setAttribute("sum", sum() );
		}
		else if("fun".equals(cmd) && "average".equals(para)){
			getApplication().setAttribute("average", (sum()/vehicleCount) );
		}		
		else {
			System.out.println("Invalid Command: "+ request.getQueryString());
		}
	}
	
	private Float getPersistentSum(){
		 Float sum;
		 ServletContext application = getApplication();
		 sum = (Float)application.getAttribute("sum");
		if ( sum == null ) sum = 0.0f;
		return sum;
	}
	
	private ServletContext getApplication(){
		return getServletConfig().getServletContext();
		}

	private static String getBody(HttpServletRequest request) throws IOException {
		 StringBuilder stringBuilder = new StringBuilder();
		 BufferedReader bufferedReader = null;
		try {
		 InputStream inputStream = request.getInputStream();
		 if (inputStream != null) {
		 bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		 char[] charBuffer = new char[128];
		 int bytesRead = -1;
		 while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		 stringBuilder.append(charBuffer, 0, bytesRead);
		 }
		 } else {
		 stringBuilder.append("");
		 }
		 } finally {
		 if (bufferedReader != null) {
		 bufferedReader.close();
		 }
		 }
		return stringBuilder.toString();
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String[] params = getBody(request).split(",");
		if(params[0].equals("leave")) {
			fees.add(Integer.valueOf(params[4]));
			vehicleCount++;
		}
	}

}
