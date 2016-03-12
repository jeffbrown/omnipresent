class BootStrap {

    def init = { servletContext ->
        ['alpha', 'beta', 'gamma', 'ray'].each {
            new demo.Foobar(name: it).save()
        }
    }
    def destroy = {
    }
}
