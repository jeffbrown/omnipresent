package demo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FoobarController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Foobar.list(params), model:[foobarCount: Foobar.count()]
    }

    def show(Foobar foobar) {
        respond foobar
    }

    @Transactional
    def save(Foobar foobar) {
        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (foobar.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond foobar.errors, view:'create'
            return
        }

        foobar.save flush:true

        respond foobar, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(Foobar foobar) {
        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (foobar.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond foobar.errors, view:'edit'
            return
        }

        foobar.save flush:true

        respond foobar, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Foobar foobar) {

        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        foobar.delete flush:true

        render status: NO_CONTENT
    }
}
