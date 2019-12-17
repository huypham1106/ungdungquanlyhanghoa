package inventory.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import inventory.dao.CateroryDAO;
import inventory.dao.ProductInfoDAO;
import inventory.model.Category;
import inventory.model.Paging;
import inventory.model.ProductInfo;
import inventory.util.ConfigLoader;

// 1 service chung 2 cái trong category ,đi theo menu cha ,bên trong category sẽ có category và productInfor

@Service
public class ProductService {
	@Autowired
	private CateroryDAO<Category> cateroryDAO;
	@Autowired
	private ProductInfoDAO<ProductInfo> productInfoDAO;
	private static final Logger log=Logger.getLogger(ProductService.class);
	//viết các phương thức
	
	public void saveCategory(Category category) throws Exception//insert
	{
		log.info("Insert Category "+category.toString());
		category.setActiveFlag(1);// khi lưu dc rồi thì bật lên 1
		category.setCreateDate(new Date());
		category.setUpdateDate(new Date());
		cateroryDAO.save(category);
	}
	public void updateCategory(Category category) throws Exception
	{
		log.info("Update Category "+category.toString());
		category.setUpdateDate(new Date());
		cateroryDAO.update(category);
	}
	
	public void deleteCategory(Category category) throws Exception// vì ko xóa hẳn nên sẽ update trạng thái về 0 ,xóa ảo
	{
		category.setActiveFlag(0);
		category.setUpdateDate(new Date());
		log.info("Delete Category "+category.toString());
		cateroryDAO.update(category);
	}
	
	public List<Category> findCategory(String property,Object value)
	{
		log.info("==== find by property category start ====");
		log.info("Property "+property+"value" +value.toString());
		return cateroryDAO.findByProperty(property, value);
	}
	public List<Category> getAllCategory(Category category,Paging paging) // kết hợp vs hàm search để lấy dữ liệu lên
	{
		log.info("show all category");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>(); // hàm search chuyển theo key và value
		if(category!=null)
		{
			if(category.getId()!=null && category.getId()!=0)
			{
				queryStr.append(" and model.id=:id");
				mapParams.put("id", category.getId());
			}
			if(category.getCode()!=null && !StringUtils.isEmpty(category.getCode()))
			{
				queryStr.append(" and model.code=:code");
				mapParams.put("code", category.getCode());
			}
			if(category.getName()!=null&& !StringUtils.isEmpty(category.getName()))
			{
				queryStr.append(" and model.name like:name"); // sét cứng đổi chữ like thằng dấu =
				mapParams.put("name", "%"+category.getName()+"%"); // cách search tên theo tương đối ko cần chính xác hết +"%"
			}
		}
		return cateroryDAO.fillAll(queryStr.toString(),mapParams,paging);
	}
	public Category findByIdCategory(int id)
	{
		log.info("find category by id "+id);
		return cateroryDAO.findById(Category.class, id);
	}
	
	// thêm phương thức cho product infoo
	public void saveProductInfo(ProductInfo productInfo)  throws Exception{
		log.info("Insert productInfo "+productInfo.toString());
		productInfo.setActiveFlag(1);
		productInfo.setCreateDate(new Date());
		productInfo.setUpdateDate(new Date());
		String fileName = System.currentTimeMillis()+"_"+productInfo.getMultipartFile().getOriginalFilename();
		processUploadFile(productInfo.getMultipartFile(),fileName);
		productInfo.setImgUrl("/upload/"+fileName); // save file hình ảnh vào db chỉ cần set dường dẫn còn spring sẽ tự mò tới
		productInfoDAO.save(productInfo);
	}
	public void updateProductInfo(ProductInfo productInfo) throws Exception {
		log.info("Update productInfo "+productInfo.toString());
		
		if(!productInfo.getMultipartFile().getOriginalFilename().isEmpty())	// kiểm tra khi mình edit ảnh xong coi nó có bị null ko
		{
			
			String fileName = System.currentTimeMillis()+"_"+productInfo.getMultipartFile().getOriginalFilename();
			processUploadFile(productInfo.getMultipartFile(),fileName);
			productInfo.setImgUrl("/upload/"+fileName); // 1 file up lên nó sẽ set lại đường dẫn
		}
		productInfo.setUpdateDate(new Date());
		productInfoDAO.update(productInfo);
	}
	public void deleteProductInfo(ProductInfo productInfo) throws Exception{
		productInfo.setActiveFlag(0);
		productInfo.setUpdateDate(new Date());
		log.info("Delete productInfo "+productInfo.toString());
		productInfoDAO.update(productInfo);
	}
	public List<ProductInfo> findProductInfo(String property , Object value){
		log.info("=====Find by property productInfo start====");
		log.info("property ="+property +" value"+ value.toString());
		return productInfoDAO.findByProperty(property, value);
	}
	public List<ProductInfo> getAllProductInfo(ProductInfo productInfo,Paging paging){
		log.info("show all productInfo");
		StringBuilder queryStr = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		if(productInfo!=null) {
			if(productInfo.getId()!=null && productInfo.getId()!=0) {
				queryStr.append(" and model.id=:id");
				mapParams.put("id", productInfo.getId());
			}
			if(productInfo.getCode()!=null && !StringUtils.isEmpty(productInfo.getCode())) {
				queryStr.append(" and model.code=:code");
				mapParams.put("code", productInfo.getCode());
			}
			if(productInfo.getName()!=null && !StringUtils.isEmpty(productInfo.getName()) ) {
				queryStr.append(" and model.name like :name");
				mapParams.put("name", "%"+productInfo.getName()+"%");
			}
		}
	
		return productInfoDAO.fillAll(queryStr.toString(), mapParams,paging);
	}
	public ProductInfo findByIdProductInfo(int id) {
		log.info("find productInfo by id ="+id);
		return productInfoDAO.findById(ProductInfo.class, id);
	}
	
	private void processUploadFile(MultipartFile multipartFile ,String fileName) throws IllegalStateException, IOException
	{
		if(!multipartFile.getOriginalFilename().isEmpty()) // có tên file mà khác rỗng
		{
			File dir = new File(ConfigLoader.getInstance().getValue("upload.location")); // trỏ tới đường dẫn file trong config.properties
			if(!dir.exists()) // nếu chưa tồn tại thì tạo file
			{
				dir.mkdirs();	
			}
			
			File file = new File(ConfigLoader.getInstance().getValue("upload.location"),fileName);
			multipartFile.transferTo(file); // file dc up lên sẽ chuyển vào file
		}
	}

	
} 




