package org.jenkinsci.plugins.gitUserContent;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.security.ACL;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import jenkins.model.Jenkins;
import org.jenkinsci.main.modules.sshd.SSHD;
import org.jenkinsci.plugins.gitserver.FileBackedHttpGitRepository;

import javax.inject.Inject;
import java.io.File;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Exposes Git repository at http://server/jenkins/userContent.git
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class GitUserContentRepository extends FileBackedHttpGitRepository implements UnprotectedRootAction {
    @Inject
    public SSHD sshd;

    public GitUserContentRepository() {
        super(new File(Jenkins.getInstance().root, "userContent"));
    }

    @Override
    protected void checkPushPermission() {
        Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "userContent.git";
    }

    @Override
    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        if (!Jenkins.get().hasPermission(Jenkins.READ) && ACL.isAnonymous2(Jenkins.getAuthentication2())) {
            rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rsp.setHeader("WWW-Authenticate", "Basic realm=\"Jenkins user\"");
            return;
        }
        Jenkins.get().checkPermission(Jenkins.READ);
        super.doDynamic(req, rsp);
    }
}
