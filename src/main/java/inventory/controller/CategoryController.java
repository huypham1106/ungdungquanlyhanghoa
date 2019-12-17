package inventory.controller;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import inventory.model.Category;
import inventory.model.Paging;
import inventory.service.ProductService;
import inventory.util.Constant;
import inventory.validate.CategoryValidator;

@Controller
public class CategoryController {
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryValidator categoryValidator;
	static final Logger log= Logger.getLogger(CategoryController.class);
	@InitBinder
	private void initBinder(WebDataBinder binder)
	{
		if(binder.getTarget()== null)
		{
			return;
		}
		SimpleDateFormat sdf =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf,true));
		if(binder.getTarget().getClass()==Category.class)
		{
			binder.setValidator(categoryValidator);
		}
	}
	
	//hàm này dùng để khi gọi đến category/list thì nó sẽ chuyển đến trang đầu tiên
	@RequestMapping(value= {"/category/list","/category/list/"})
	public String redirect()
	{
		return "redirect:/category/list/1";
	}
	
	@RequestMapping(value="/category/list/{page}")
	public String showCategoryList(Model model,HttpSession session, @ModelAttribute("searchForm") Category category,@PathVariable("page")int page)
	{
		Paging paging= new Paging(5);
		paging.setIndexPage(page); // dựa vào trang hiện tại để tính offset bắt đầu bằng bao nhiêu
		List<Category> categories=productService.getAllCategory(category,paging);
		if(session.getAttribute(Constant.MSG_SUCCESS)!=null)
		{
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);//lưu xong rmote nó khỏi session
		}
		if(session.getAttribute(Constant.MSG_ERROR)!=null)
		{
			model.addAttribute(Constant.MSG_ERROR, session.getAttribute(Constant.MSG_ERROR));
			session.removeAttribute(Constant.MSG_ERROR);//lưu xong rmote nó khỏi session
		}
		model.addAttribute("pageInfo", paging);
		model.addAttribute("categories", categories);
		return "category-list";
	}
	@GetMapping("/category/add")
	public String add(Model model)// show ra cai form dữ liệu để người dùng điền thông tin
	{
		model.addAttribute("titlePage","Add Category");
		model.addAttribute("modelForm", new Category());	// trả lại 1 đối tượng rỗng cho người dùng tự điền vào
		model.addAttribute("viewOnly", false); // add 1 cái view để ng dùng xem những sản phẩm có sẵn
		return "category-action";
		
		}
	@GetMapping("/category/edit/{id}") // tìm theo id để hiện lên cho người dùng sửa
	public String edit(Model model,@PathVariable("id") int id)// gắn biến id này này vào getmapping
	{
		log.info("Edit category with id=" +id);
		Category category = productService.findByIdCategory(id); //có id rồi nên tìm bản ghi trùng với id lấy lên
		if(category !=null)// nếu csdl có bản ghi này
		{
			model.addAttribute("titlePage","Edit Category");
			model.addAttribute("modelForm",category);	// trả lại 1 đối tượng rỗng cho người dùng tự điền vào
			model.addAttribute("viewOnly", false);
			return "category-action";
		}
		return "redirect:/category/list";
		
	
		
	}
	@GetMapping("/category/view/{id}") 
	public String view(Model model,@PathVariable("id") int id)// gắn biến id này này vào getmapping
	{
		log.info("View category with id=" +id);
		Category category = productService.findByIdCategory(id); //có id rồi nên tìm bản ghi trùng với id lấy lên
		if(category !=null)// nếu csdl có bản ghi này
		{
			model.addAttribute("titlePage","View Category");
			model.addAttribute("modelForm",category);	// trả lại 1 đối tượng rỗng cho người dùng tự điền vào
			model.addAttribute("viewOnly", true);// khong cho sửa nên bằng true
			return "category-action";// sua cho nay ========
		}
		return "redirect:/category/list";
		
	
		
	}
	@PostMapping("/category/save")
	public String save(Model model,@ModelAttribute("modelForm") @Validated Category category,BindingResult result,HttpSession session)
	{
		if(result.hasErrors())	// form submit có lỗi thì return
		{
			if(category.getId()!=null)
			{
				model.addAttribute("titlePage","Edit Category");
				
			}else 
			{
				model.addAttribute("titlePage","Add Category");
			}
			
			model.addAttribute("modelForm",category);	// trả lại 1 đối tượng rỗng cho người dùng tự điền vào
			model.addAttribute("viewOnly", false);
			return "category-action";
		}
		if(category.getId()!=null && category.getId()!=0) // nếu bản ghi này có rồi
		{
			try {
				productService.updateCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Update success!!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
				session.setAttribute(Constant.MSG_ERROR, "Update has error");
			}
			//model.addAttribute("message", "Update success!!!!");
		}
		else
		{
			try {
				productService.saveCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert success!!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				session.setAttribute(Constant.MSG_ERROR, "Insert has error!!!");
			}
			//model.addAttribute("message", "Insert success");
		}
		//return showCategoryList(model); // chuyển tới hàm category list ở trên	(khong lưu vào model nữa)
		return "redirect:/category/list";
	}
	@GetMapping("/category/delete/{id}")// truyền id để biết xóa bản ghi nao
	public String delete(Model model,@PathVariable("id") int id,HttpSession session)// gắn biến id này này vào getmapping
	{
		log.info("Delete category with id=" +id);
		Category category = productService.findByIdCategory(id); //có id rồi nên tìm bản ghi trùng với id lấy lên
		if(category!=null)
		{
			try {
				productService.deleteCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "delete success !!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				session.setAttribute(Constant.MSG_ERROR, "delete has error !!");
			}
		}
		return "redirect:/category/list";
		
	
		
	}
	
	
	
}





