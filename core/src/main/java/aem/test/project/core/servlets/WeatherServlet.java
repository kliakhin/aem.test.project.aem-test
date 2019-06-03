package aem.test.project.core.servlets;

import aem.test.project.core.bean.WeatherItems;
import aem.test.project.core.service.WeatherService;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;

@SlingServlet(
        label = "Samples - Sling All Methods Servlet",
        description = "Sample implementation of a Sling All Methods Servlet.",
        paths = {"/services/hello"},
        methods = {"GET"},
        extensions = {"html", "htm"})
public class WeatherServlet extends SlingAllMethodsServlet {

    @Reference
    private WeatherService weatherService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        RequestParameter city = getCityParameter(request);

        printWeather(response, city);
    }

    private RequestParameter getCityParameter(SlingHttpServletRequest request) {
        return request.getRequestParameter("city");
    }

    private void printWeather(SlingHttpServletResponse response, RequestParameter city) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        WeatherItems items;

        if (city != null) {
            String cityString = city.getString();
            items = weatherService.getWeather(cityString);
            outputStream.print(items.toString());
        } else {
            outputStream.print("Need add city as parameter");
        }

        response.setStatus(200);
    }
}
