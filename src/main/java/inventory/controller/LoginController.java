package inventory.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import inventory.model.Auth;
import inventory.model.Menu;
import inventory.model.Role;
import inventory.model.UserRole;
import inventory.model.Users;
import inventory.service.UserService;
import inventory.util.Constant;
import inventory.validate.LoginValidator;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private LoginValidator loginValidator;
	@InitBinder
	private void initBinder(WebDataBinder binder)
	{
		if(binder.getTarget()==null)
			return;
		if(binder.getTarget().getClass()==Users.class)
		{
			binder.setValidator(loginValidator);
		}
	}
	@GetMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("loginForm", new Users());
		return "login/login";
	}
	@PostMapping("/processLogin")
	public String processLogin(Model model ,@ModelAttribute("loginForm")@Validated Users users ,BindingResult result ,HttpSession session)
	{
		if(result.hasErrors())
		{
			return "login/login";
		}
		Users user = userService.findByProperty("userName", users.getUserName()).get(0);
		UserRole userRole =(UserRole) user.getUserRoles().iterator().next();
		List<Menu> menuList =  new ArrayList<>();
		Role role = userRole.getRole();
		List<Menu> menuChildList = new ArrayList<>();
		for(Object obj: role.getAuths())
		{
			Auth auth = (Auth) obj;
			Menu menu = auth.getMenu();
			if(menu.getParentId()==0 &&  menu.getOrderIndex()!=-1 && menu.getActiveFlag()==1 && auth.getPermission()==1 && auth.getActiveFlag()==1)
			{
				menu.setIdMenu(menu.getUrl().replace("/","")+"Id"); // /category/list ==> categoryListId
				menuList.add(menu);
			}
			else if(menu.getParentId()!=0 &&  menu.getOrderIndex()!=-1 && menu.getActiveFlag()==1 && auth.getPermission()==1 && auth.getActiveFlag()==1)
			{
				menu.setIdMenu(menu.getUrl().replace("/","")+"Id"); // /category/list ==> categoryListId
				menuChildList.add(menu);
			}
		}
		for(Menu menu :menuList)// m?i m?t v?ng for sinh ra m?t cái list m?i add th?ng con vào
		{
			List<Menu> childList = new ArrayList<>();
			for(Menu childMenu :menuChildList)
			{
				if (childMenu.getParentId() == menu.getId())// th?ng con get id b?ng v?i id c?a th?ng cha
				{
					childList.add(childMenu);
				}
			}
			menu.setChild(childList);// k?t thúc v?ng for s? gán mene cho childList
		}
		sortMenu(menuList);// s?p x?p th?ng cha trư?c
		for(Menu menu: menuList)// v?ng for s?p x?p th?ng con
		{
			sortMenu(menuChildList);
		}
		session.setAttribute(Constant.MENU_SESSION, menuList); // lưu gi? cái thông tin nào vào session
		session.setAttribute(Constant.USER_INFO, user);
		return "redirect:/index";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied()
	{
		return "access-denied";
		
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) //v? ? trên đ? gán username vs pass cho 1 bi?n session nên làm 1 hàm g? nó ra
	{
		session.removeAttribute(Constant.MENU_SESSION);
		session.removeAttribute(Constant.USER_INFO);
		
		return "redirect:/login"; //chuy?n đ?n l?i trang index
	}
	
	private void sortMenu(List<Menu> menus) // s?p x?p menu theo th? t? order index
	{
		Collections.sort(menus , new Comparator<Menu>()
		{

			@Override
			public int compare(Menu o1, Menu o2) 
			{
				return o1.getOrderIndex() - o2.getOrderIndex();// s?p theo th? t? tăng d?n
			}
		}); 
	}
}
