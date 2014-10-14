package org.jenkinsci.plugins.gitUserContent;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkinsci.main.modules.sshd.SSHD;
import org.jenkinsci.plugins.gitserver.FileBackedHttpGitRepository;

import javax.inject.Inject;
import java.io.File;

/**
 * Exposes Git repository at http://server/jenkins/userContent.git
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class GitUserContentRepository extends FileBackedHttpGitRepository implements RootAction {
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
}
