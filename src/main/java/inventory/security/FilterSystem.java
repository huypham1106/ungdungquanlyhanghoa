package inventory.security;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import inventory.model.Auth;
import inventory.model.UserRole;
import inventory.model.Users;
import inventory.util.Constant;

public class FilterSystem implements HandlerInterceptor 
// tạo 1 hàm bộ lọc imp một 1 bộ sẵn có của spring ,khi sử dụng đi spring->bộ lọc
{
	
	Logger logger =Logger.getLogger(FilterSystem.class); // ghi action vào log để mốt xem lại
	//khi 1 url đi qua spring nó sẽ đi qua hàm này trước sau đó mới đến controller
	// sẽ kiểm tra trước rồi mới được đi đến controller ko dc truy cập thì quay lại hàm này
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("Request URI ="+request.getRequestURI());// ghi chú lại người dùng đang request đến đâu trong hệ thống
		Users users=(Users) request.getSession().getAttribute(Constant.USER_INFO);//kiem tra đã login hay chưa bằng cách kiểm tra biến session đã có lưu user va pass chưa
		if(users == null)
		{
			response.sendRedirect(request.getContextPath()+"/login");//nếu user null trả về trang logion ,getContextPath():trả full url/login
			return false;
		}
		if(users !=null)
		{
			String url = request.getServletPath();
			if(!hasPermission(url, users))//nếu ko có quyền chuyển sang trang error
			{
				response.sendRedirect(request.getContextPath()+"/access-denied");
				return false;
			}
		}
		
		return true;
	}
	private boolean hasPermission(String url ,Users users)  //kiểm tra user này có dc quyền truy cập trang này không
	{
		if(url.contains("/index") || url.contains("/access-denied")|| url.contains("/logout"))
		{
			return true;
		}
		UserRole userRole=(UserRole) users.getUserRoles().iterator().next();
		Set<Auth> auths =userRole.getRole().getAuths();
		for(Object obj:auths)
		{
			Auth auth=(Auth) obj;
			if(url.contains(auth.getMenu().getUrl()))//kiểm tra url từ bảng auth có khớp vs url bảng menu ko
			{
				return auth.getPermission()==1;//nếu có trả lời 1 có quyền truy cập , ngc lại false
			}
		}
		return false;
		
	}
	// sau khi controller trả về sẽ đi đến hàm này
	/*
	 * @Override public void postHandle(HttpServletRequest request,
	 * 
	 */
}
