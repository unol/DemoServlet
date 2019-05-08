import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public float sum() {
		float f = 0;

		for (int i : fees) {
			f += i;
		}
		return f / 100;
	}

	private String formatTime(float time) {
		char ext = 's';
		if (time >= 60) {
			time /= 60;
			ext = 'm';
			if (time >= 60) {
				time /= 60;
				ext = 'h';
			}
		}
		return Float.toString(round(time, 1)) + ext;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String para = request.getParameter("cmd");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		if ("sum".equals(para)) {
			out.println(round(sum(), 2) + " Euro");
		} else if ("average".equals(para)) {
			out.println(round(sum() / vehicleCount, 2) + " Euro");
		} else if ("total_time".equals(para)) {
			out.println(formatTime(sum()));
		} else {
			System.out.println("Invalid Command: " + request.getQueryString());
		}
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] params = getBody(request).split(",");
		if (params[0].equals("leave")) {
			fees.add(Integer.valueOf(params[4]));
			vehicleCount++;
		}
	}

}
