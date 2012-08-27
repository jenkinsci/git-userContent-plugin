def l=namespace(lib.LayoutTagLib)

l.layout {
    l.main_panel {
        h1 "Accessing UserContent"

        p {
            raw _("blurb",app.rootUrl)
        }
        pre {
            def url = "${app.rootUrl}userContent.git"
            raw "git clone <a href='${url}'>${url}</a>\n"

            if (my.sshd.actualPort>0) {
                raw "git clone ssh://${new URL(app.rootUrl).host}:${my.sshd.actualPort}/userContent.git"
            }
        }
    }
}
