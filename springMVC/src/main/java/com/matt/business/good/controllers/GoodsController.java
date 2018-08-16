package com.matt.business.good.controllers;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.matt.business.good.entities.Goods;
import com.matt.business.good.service.GoodsService;
import com.matt.business.good.view.ViewExcel;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Resource
	GoodsService goodsService;
	
	@RequestMapping("/test")
	public String test()
	{
		return "goods/list";
	}
	
	/*
	 * 产品列表与分页Action
	 */
	@RequestMapping("/list")
	public String list(Model model,@RequestParam(required=false,defaultValue="1") int pageNO){
		int size=5;
		model.addAttribute("size",size);
		model.addAttribute("pageNO",pageNO);
		model.addAttribute("count",goodsService.getGoodsCount());
		model.addAttribute("goods", goodsService.getGoodsPager(pageNO, size));
		return "goods/list";
	}
//	
//	/*
//	 * 删除单个产品对象Action
//	 */
//	@RequestMapping("/delete/{id}")
//	public String delete(Model model,@PathVariable int id,@RequestParam(required=false,defaultValue="1") int pageNO,RedirectAttributes redirectAttributes){
//		if(goodsService.delete(id)>0)
//		{
//			redirectAttributes.addFlashAttribute("message", "删除成功！");
//		}else{
//			redirectAttributes.addFlashAttribute("message", "删除失败！");
//		}
//		return "redirect:/goods/list?pageNO="+pageNO;
//	}
//	
//	/*
//	 * 删除多个产品对象Action
//	 */
//	@RequestMapping("/deletes")
//	public String deletes(Model model,@RequestParam int[] id,@RequestParam(required=false,defaultValue="1") int pageNO,RedirectAttributes redirectAttributes){
//		//执行删除
//		int rows=goodsService.deletes(id);
//		if(rows>0)
//		{
//			redirectAttributes.addFlashAttribute("message", "删除"+rows+"行记录成功！");
//		}else{
//			redirectAttributes.addFlashAttribute("message", "删除失败！");
//		}
//		return "redirect:/goods/list?pageNO="+pageNO;
//	}
//	
	/*
	 * 添加商品
	 */
	@RequestMapping("/add")
	public String add(Model model){
		model.addAttribute("entity", new Goods());
		return "goods/add";
	}
	
	/*
	 * 添加商品保存
	 */
	@RequestMapping("/addSave")
	public String addSave(Model model,@ModelAttribute("entity") Goods entity,BindingResult bindingResult){
		//如果模型中存在错误
		if(!bindingResult.hasErrors()){
			if(goodsService.insert(entity)>0)
			{
				return "redirect:/goods/list";	
			}
		}
		model.addAttribute("entity", entity);
		return "goods/add";
	}
	
	/*
	 * 编辑商品
	 */
	@RequestMapping("/edit/{id}")
	public String edit(Model model,@PathVariable int id){
		model.addAttribute("entity", goodsService.getGoodsById(id));
		return "goods/edit";
	}
	
	/*
	 * 编辑商品保存
	 */
	@RequestMapping("/editSave")
	public String editSave(Model model,@ModelAttribute("entity") Goods entity,BindingResult bindingResult){
		//如果模型中存在错误
		if(!bindingResult.hasErrors()){
			if(goodsService.update(entity)>0)
			{
				return "redirect:list";	
			}
		}
		model.addAttribute("entity", entity);
		return "/goods/edit";
	}
	
	/**
	 * 上传图片
	 */
	@RequestMapping("/upPicture/{id}")
	public String upPicture(Model model,@PathVariable int id){
		model.addAttribute("entity", goodsService.getGoodsById(id));
		return "goods/upfile";
	}
	
	/*
	 * 上传图片保存
	 */
	@RequestMapping("/upPictureSave/{id}")
	public String upPictureSave(Model model,@PathVariable int id,MultipartFile picFile,HttpServletRequest request){
		Goods entity=goodsService.getGoodsById(id);
		//如果选择了文件
		if(picFile!=null){ 
			//如果文件大小不为0
			if(picFile.getSize()>0){
				//获得上传位置
				String path=request.getServletContext().getRealPath("/images");
				//生成文件名
				String filename=UUID.randomUUID().toString()+picFile.getOriginalFilename().substring(picFile.getOriginalFilename().lastIndexOf("."));
				File tempFile=new File(path, filename);
				try {
					//保存文件
					picFile.transferTo(tempFile);
					//更新数据
					entity.setPicture(filename);
					goodsService.update(entity);
					//转向列表页
					return "redirect:/goods/list";	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		model.addAttribute("entity", entity);
		return "goods/upfile";
	}
	
	
	  /**
	 * @throws Exception  
	* @Title: exportExcel 
	* @Description: 导出用户数据生成的excel文件
	* @param  model
	* @param  request
	* @param  response
	* @param  设定文件 
	* @return ModelAndView    返回类型 
	* @throws 
	*/
	@RequestMapping(value="/exportExcel.shtml",method=RequestMethod.POST)  
    public ModelAndView exportExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {  
       ViewExcel viewExcel = new ViewExcel();    
       Map<String, Object> obj = null;
       
       //获取数据库表生成的workbook
       Map<String, Object> condition = new HashMap<String, Object>();
       HSSFWorkbook workbook = resultSetToExcel("123.xls");
       try {
    	   viewExcel.buildExcelDocument(obj, workbook, request, response);
       } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
       }
       return new ModelAndView(viewExcel, model);   
   }  
	
	
	
	/** 
	* @Title: resultSetToExcel 
	* @Description: 根据结果集生成excel
	* @param  rs 数据集
	* @param  sheetName 工作表名称
	* @return HSSFWorkbook    返回类型 
	* @throws 
	*/
	public  HSSFWorkbook resultSetToExcel(String sheetName) throws Exception
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("参与抽奖活动人员名单");
		//workbook.setSheetName(0,sheetName,HSSFWorkbook..ENCODING_UTF_16);
		HSSFRow row= sheet.createRow((short)0);
		HSSFCell cell = null;
		
		List<String> md = new ArrayList<String>();
		for (int i=0; i<10; i++)
		{
			md.add(i + "");
		}
		
//		//写入各个字段的名称
//		for(int i=1;i<=md.size();i++) {
//			  cell = row.createCell((i-1));
//		      cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//		      cell.setCellValue(md.get(i));
//		}
	
		for (int i =1 ; i<5;i++)
		{
			row= sheet.createRow((short)i);
			for(int j=1;j<md.size();j++) {
				cell = row.createCell(j-1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(md.get(j));
			}
		}
	
		return workbook;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
