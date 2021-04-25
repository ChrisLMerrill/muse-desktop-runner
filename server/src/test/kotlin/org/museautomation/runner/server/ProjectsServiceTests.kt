package org.museautomation.runner.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jboss.resteasy.core.SynchronousDispatcher
import org.jboss.resteasy.core.SynchronousExecutionContext
import org.jboss.resteasy.mock.MockDispatcherFactory
import org.jboss.resteasy.mock.MockHttpRequest
import org.jboss.resteasy.mock.MockHttpResponse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.museautomation.runner.projects.RegisteredProject
import org.museautomation.runner.projects.RegisteredProjectStore
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectsServiceTests
{
    private val dispatcher = MockDispatcherFactory.createDispatcher()
    private val store = mock(RegisteredProjectStore::class.java)
    private val service = ProjectsService()
    private val mapper = ObjectMapper().registerKotlinModule()
    private val project1 = RegisteredProject("project1", "Project 1", "/path/to/project1/")
    private val project2 = RegisteredProject("project2", "Project 2", "/path/to/project2/")

    @BeforeEach
    fun setup()
    {
        Mockito.`when`(store.get("project1"))
            .thenReturn(project1)
        Mockito.`when`(store.get("project2"))
            .thenReturn(project2)
        Mockito.`when`(store.get("project-missing")).thenReturn(null)
        service.project_store = store
        dispatcher.registry.addSingletonResource(service)
    }

    @Test
    fun getFirstProject()
    {
        val result = sendGetProjectByIdRequest("project1")
        assertEquals(Response.Status.OK.statusCode, result.response.status)
        assertNotNull(result.project)
        assertEquals("Project 1", result.project?.name)
    }

    @Test
    fun getSecondProject()
    {
        val result = sendGetProjectByIdRequest("project2")
        assertEquals(Response.Status.OK.statusCode, result.response.status)
        assertNotNull(result.project)
        assertEquals("Project 2", result.project?.name)
    }

    @Test
    fun getMissingProject()
    {
        val result = sendGetProjectByIdRequest("missing")
        assertEquals(Response.Status.NOT_FOUND.statusCode, result.response.status)
        assertNull(result.project)
    }

    @Test
    fun getProjects()
    {
        Mockito.`when`(store.getAll()).thenReturn(mutableListOf(project1, project2))

        val projects = sendGetProjectList()
        assertNotNull(projects)
        assertEquals(2, projects.size)
        assertTrue(projects.contains(project1))
        assertTrue(projects.contains(project2))
    }

    @Test
    fun getProjectsEmpty()
    {
        Mockito.`when`(store.getAll()).thenReturn(emptyList())

        val projects = sendGetProjectList()
        assertNotNull(projects)
        assertEquals(0, projects.size)
        assertFalse(projects.contains(project1))
        assertFalse(projects.contains(project2))
    }

    private fun sendGetProjectList(): List<RegisteredProject>
    {
        val request = MockHttpRequest.get("/project/list")
        request.accept(MediaType.APPLICATION_JSON_TYPE)
        val response = MockHttpResponse()
        request.asynchronousContext =
            SynchronousExecutionContext(dispatcher as SynchronousDispatcher, request, response)
        dispatcher.invoke(request, response)

        assertEquals(Response.Status.OK.statusCode, response.status)
        return mapper.readValue(response.contentAsString)
    }

    data class ResponseAndProject(val response: MockHttpResponse, val project: RegisteredProject?)

    private fun sendGetProjectByIdRequest(id: String): ResponseAndProject
    {
        val request = MockHttpRequest.get("/project/id/$id")
        request.accept(MediaType.APPLICATION_JSON_TYPE)
        val response = MockHttpResponse()
        request.asynchronousContext =
            SynchronousExecutionContext(dispatcher as SynchronousDispatcher, request, response)
        dispatcher.invoke(request, response)

        val content = response.contentAsString
        return if (content != null && content.isNotEmpty())
            ResponseAndProject(response, mapper.readValue(content, RegisteredProject::class.java))
        else
            ResponseAndProject(response, null)
    }

    @Test
    fun getProjectByIdNotFound()
    {
        assertNull(store.get("project-missing"))
    }
}