package aem.test.project.core.servlets;

import com.adobe.cq.social.user.api.UserProfile;
import com.adobe.granite.security.user.UserProperties;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.base.util.AccessControlUtil;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

@SlingServlet(
        methods = {"GET"},
        paths = {"/services/hello"},
        extensions = {"html", "htm"}
)
public class HelloServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        UserProperties userProperties = request.adaptTo(UserProperties.class);


        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        String userID = session.getUserID();

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(userID.getBytes());
        try {
            UserManager userManager = AccessControlUtil.getUserManager(session);
            Authorizable authorizable = userManager.getAuthorizable(userID);
            outputStream.write(authorizable.toString().getBytes());
            outputStream.write(authorizable.getPath().getBytes());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
}
