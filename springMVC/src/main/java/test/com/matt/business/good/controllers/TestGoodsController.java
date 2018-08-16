package test.com.matt.business.good.controllers;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.matt.business.good.controllers.GoodsController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")  
@ContextConfiguration({"classpath:/WEB-INF/springMVC-servlet.xml","classpath:/WEB-INF/applicationContext.xml"})  
public class TestGoodsController {

	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private GoodsController goodsController;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}
	
	@Test
	public void testList() throws Exception {
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/goods/list"))
				.andExpect(MockMvcResultMatchers.view().name("goods/list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("goods"))
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		
	}
	
	@Test
	public void testAdd() throws Exception 
	{
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/goods/add"))
				.andExpect(MockMvcResultMatchers.view().name("goods/add"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("entity"))
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	
	
	@Test
	public void testLogin() throws Exception {
		request.setAttribute("123", "345");
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/goods/edit/1"))
		.andExpect(MockMvcResultMatchers.view().name("goods/edit"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("entity"))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		
		String content = result.getResponse().getContentAsString();
		System.out.println("====" + content);
		
		Assert.assertNotNull(result.getModelAndView().getModel().get("entity"));
//		assertEquals("", goodsController.add(model));
	}
}
