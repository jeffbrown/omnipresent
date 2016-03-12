package demo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FoobarController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Foobar.list(params), model:[foobarCount: Foobar.count()]
    }

    def show(Foobar foobar) {
        respond foobar
    }

    def create() {
        respond new Foobar(params)
    }

    @Transactional
    def save(Foobar foobar) {
        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (foobar.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond foobar.errors, view:'create'
            return
        }

        foobar.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'foobar.label', default: 'Foobar'), foobar.id])
                redirect foobar
            }
            '*' { respond foobar, [status: CREATED] }
        }
    }

    def edit(Foobar foobar) {
        respond foobar
    }

    @Transactional
    def update(Foobar foobar) {
        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (foobar.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond foobar.errors, view:'edit'
            return
        }

        foobar.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'foobar.label', default: 'Foobar'), foobar.id])
                redirect foobar
            }
            '*'{ respond foobar, [status: OK] }
        }
    }

    @Transactional
    def delete(Foobar foobar) {

        if (foobar == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        foobar.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'foobar.label', default: 'Foobar'), foobar.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'foobar.label', default: 'Foobar'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
