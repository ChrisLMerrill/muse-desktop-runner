package org.museautomation.runner.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jboss.resteasy.core.SynchronousDispatcher
import org.jboss.resteasy.core.SynchronousExecutionContext
import org.jboss.resteasy.mock.MockDispatcherFactory
import org.jboss.resteasy.mock.MockHttpRequest
import org.jboss.resteasy.mock.MockHttpResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.museautomation.runner.projects.*
import org.museautomation.runner.server.responses.ErrorResponse
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectsServiceTests
{
    private val dispatcher = MockDispatcherFactory.createDispatcher()
    private lateinit var store: RegisteredProjectStore
    private val service = ProjectsService()
    private val mapper = ObjectMapper().registerKotlinModule()
    private val project1 = RegisteredProject("project1", "Project 1", "/path/to/project1/")
    private val project2 = RegisteredProject("project2", "Project 2", "/path/to/project2/")

    @BeforeAll
    fun setupOnce()
    {
        dispatcher.registry.addSingletonResource(service)
        dispatcher.providerFactory.registerProvider(JacksonProvider::class.java)
    }

    @BeforeEach
    fun setupEachTest()
    {
        store = mock(RegisteredProjectStore::class.java)
        service.project_store = store
    }

    private fun setupProjects()
    {
        Mockito.`when`(store.get("project1"))
            .thenReturn(project1)
        Mockito.`when`(store.get("project2"))
            .thenReturn(project2)
        Mockito.`when`(store.get("missing")).thenReturn(null)
    }

    @Test
    fun getFirstProject()
    {
        setupProjects()
        val result = sendGetProjectByIdRequest("project1")
        assertEquals(Response.Status.OK.statusCode, result.response.status)
        assertNotNull(result.project)
        assertEquals("Project 1", result.project?.name)
    }

    @Test
    fun getSecondProject()
    {
        setupProjects()
        val result = sendGetProjectByIdRequest("project2")
        assertEquals(Response.Status.OK.statusCode, result.response.status)
        assertNotNull(result.project)
        assertEquals("Project 2", result.project?.name)
    }

    @Test
    fun getMissingProject()
    {
        setupProjects()
        val result = sendGetProjectByIdRequest("missing")
        assertEquals(Response.Status.NOT_FOUND.statusCode, result.response.status)
        assertNull(result.project)
    }

    @Test
    fun getProjects()
    {
        setupProjects()
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
    fun installProjectUnversioned()
    {
        val project = DownloadableProjectSettings("my.url", DownloadableProject("project1", "url2", emptyList()), null)
        Mockito.`when`(store.install(project)).thenReturn(InstallResult(true, null, null))
        val response = sendInstallProject(project, "project_id1")
        assertEquals(Response.Status.OK.statusCode, response.status)
    }

    @Test
    fun installProjectDuplicateId()
    {
        val project = DownloadableProjectSettings("my.url", DownloadableProject("project1", "url2", emptyList()), null)
        Mockito.`when`(store.install(project)).thenThrow(ProjectAlreadyExistsException("project_id1"))
        val response = sendInstallProject(project, "project_id1")
        assertEquals(Response.Status.BAD_REQUEST.statusCode, response.status)
        val error = mapper.readValue(response.contentAsString, ErrorResponse::class.java)
        assertTrue(error.message.contains("project_id1"))
        assertTrue(error.message.contains("exists"))
    }

    @Test
    fun installProjectInvalidId()  // id must be ok for a filename - i.e. these are not allowed on Windows: / ? < > \ : * | "
    {
        val project = DownloadableProjectSettings("my.url", DownloadableProject("project1", "url2", emptyList()), null)
        Mockito.`when`(store.install(project)).thenThrow(IllegalProjectIdentifierException())
        for (c in "/?<>\\:*|\"") // in a loop, check each of these: / ? < > \ : * | "
        {
            val response = sendInstallProject(project, "project_id_$c")
            assertEquals(Response.Status.BAD_REQUEST.statusCode, response.status)
            val error = mapper.readValue(response.contentAsString, ErrorResponse::class.java)
            assertTrue(error.message.contains(c))
        }
    }

    private fun sendInstallProject(project: DownloadableProjectSettings, id: String): MockHttpResponse
    {
        val request = MockHttpRequest.put("/project/id/${URLEncoder.encode(id, Charset.forName("UTF-8"))}")
        request.content(mapper.writeValueAsBytes(project))
        request.contentType(MediaType.APPLICATION_JSON_TYPE)
        request.accept(MediaType.APPLICATION_JSON_TYPE)
        val response = MockHttpResponse()
        request.asynchronousContext =
            SynchronousExecutionContext(dispatcher as SynchronousDispatcher, request, response)
        dispatcher.invoke(request, response)

        return response
    }
}