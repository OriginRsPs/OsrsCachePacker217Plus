package com.mark.js5

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import com.mark.netrune.endpoint.Service
import com.mark.netrune.endpoint.SessionInitializer
import com.mark.netrune.net.netty4.DefaultEventLoopGroupFactory
import com.mark.netrune.net.server.ServerBinding
import org.jire.netrune.net.server.netty4.DefaultServerBootstrapFactory
import org.jire.netrune.net.server.netty4.Netty4Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Endpoint(
    private val service: Service,

    private val parentGroup: EventLoopGroup = DefaultEventLoopGroupFactory.eventLoopGroup(1),
    private val childGroup: EventLoopGroup = DefaultEventLoopGroupFactory.eventLoopGroup(),

    private val js5Ports: IntArray
) : Runnable, AutoCloseable {

    override fun run() {
        val bindings = bindServer()
        bindings.forEach {
            it.channelCloseFuture.get() // don't exit thread until channels are closed
            logger.warn("Channel on address \"{}\" was closed", it.localAddress)
        }
    }

    private fun bindServer(): List<ServerBinding> {
        logger.info("Binding addresses...")

        // TODO: JS5-only service on the `js5Ports`
        val bindings = (js5Ports).map {
            val childHandler: ChannelHandler = SessionInitializer(service)
            val bootstrap = DefaultServerBootstrapFactory.serverBootstrap(
                parentGroup, childGroup,
                childHandler = childHandler
            )
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.AUTO_READ, false)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)

            val server = Netty4Server(
                parentGroup, childGroup,
                bootstrap
            )
            server.bind(it)
        }

        bindings.forEach {
            it.bindFuture.get() // wait for bind to complete
            logger.info("Bound to address \"{}\"", it.localAddress)
        }

        return bindings
    }

    override fun close() {
        parentGroup.shutdownGracefully().get()
        childGroup.shutdownGracefully().get()
    }

    companion object {

        const val JS5_PORT_BASE = 50000

        private val logger: Logger = LoggerFactory.getLogger(Endpoint::class.java)

    }

}