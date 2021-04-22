package org.museautomation.runnner.server

import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.server.handlers.resource.ClassPathResourceManager
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.DeploymentManager
import org.jboss.resteasy.core.ResteasyDeploymentImpl
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer
import org.slf4j.LoggerFactory

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class RunnerServer {
    fun start(host: String, port: Int)
    {
        LOG.info("Starting server on $host:$port")

        server = UndertowJaxrsServer()

        val deployment = ResteasyDeploymentImpl()
        deployment.applicationClass = RunnerServerApplication::class.java.name
        deployment.injectorFactoryClass = "org.jboss.resteasy.cdi.CdiInjectorFactory"

        val deployment_info = server.undertowDeployment(deployment, "/")
        deployment_info.setClassLoader(RunnerServer::class.java.classLoader)
        deployment_info.setDeploymentName("Muse Runner-Server")
        deployment_info.setContextPath("/ws")
        deployment_info.addListener(Servlets.listener(org.jboss.weld.environment.servlet.Listener::class.java))

        server.deploy(deployment_info)

        server.addResourcePrefixPath("/",
            Handlers.resource(ClassPathResourceManager(RunnerServer::class.java.classLoader)))

        val builder = Undertow.builder().addHttpListener(port, host)
        server.start(builder)

        LOG.info("server started")
    }

    /**
     * Stop server.
     */
    fun stopServer()
    {
        checkNotNull(server) { "Server has not been started yet" }
        LOG.info("Stopping server")
        deployment_manger.undeploy()
        server.stop()
        LOG.info("Server stopped")
    }

    lateinit var server : UndertowJaxrsServer
    lateinit var deployment_manger: DeploymentManager

    companion object
    {
        private val LOG = LoggerFactory.getLogger(RunnerServer::class.java)
    }
}

fun main(args: Array<String>)
{
     RunnerServer().start(args[0], Integer.parseInt(args[1]))
}