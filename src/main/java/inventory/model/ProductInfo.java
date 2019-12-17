package inventory.model;
// Generated Oct 6, 2019 11:45:56 PM by Hibernate Tools 5.1.10.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.web.multipart.MultipartFile;

/**
 * ProductInfo generated by hbm2java
 */
@Entity
@Table(name = "product_info", catalog = "inventory_management")
public class ProductInfo implements java.io.Serializable {

	private Integer id;
	private Category category;
	private String code;
	private String name;
	private String description;
	private String imgUrl;
	private int activeFlag;
	private Date createDate;
	private Date updateDate;
	/*
	 * private Set histories = new HashSet(0); private Set productInStocks = new
	 * HashSet(0); private Set invoices = new HashSet(0);
	 */
	
	 private Set<History> histories = new HashSet<History>(0); 
	 private Set<ProductInStock> productInStocks = new HashSet<ProductInStock>(0); 
	 private Set<Invoice> invoices = new HashSet<Invoice>(0);
	 
	private MultipartFile multipartFile; // của spring fw khi ta upload file hình ảnh lên nó sẽ gán vào biến này
	private Integer cateId;
	public ProductInfo() {
	}

	public ProductInfo(Category category, String code, String name, String imgUrl, int activeFlag, Date createDate,
			Date updateDate) {
		this.category = category;
		this.code = code;
		this.name = name;
		this.imgUrl = imgUrl;
		this.activeFlag = activeFlag;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public ProductInfo(Category category, String code, String name, String description, String imgUrl, int activeFlag,
			Date createDate, Date updateDate, Set histories, Set productInStocks,
			Set invoices) {
		this.category = category;
		this.code = code;
		this.name = name;
		this.description = description;
		this.imgUrl = imgUrl;
		this.activeFlag = activeFlag;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.histories = histories;
		this.productInStocks = productInStocks;
		this.invoices = invoices;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATE_ID", nullable = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "IMG_URL", nullable = false, length = 200)
	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Column(name = "ACTIVE_FLAG", nullable = false)
	public int getActiveFlag() {
		return this.activeFlag;
	}

	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false, length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE", nullable = false, length = 19)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
	  @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public
	  Set<History> getHistories() { return this.histories; }
	  
	  public void setHistories(Set<History> histories) { this.histories =
	  histories; }
	  
	  @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public
	  Set<ProductInStock> getProductInStocks() { return this.productInStocks; }
	  
	  public void setProductInStocks(Set<ProductInStock> productInStocks) {
	  this.productInStocks = productInStocks; }
	  
	  @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public
	  Set<Invoice> getInvoices() { return this.invoices; }
	  
	  public void setInvoices(Set<Invoice> invoices) { this.invoices = invoices; }
	 

	
	
	

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public Set
	 * getHistories() { return this.histories; }
	 * 
	 * public void setHistories(Set histories) { this.histories = histories; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public Set
	 * getProductInStocks() { return this.productInStocks; }
	 * 
	 * public void setProductInStocks(Set productInStocks) { this.productInStocks =
	 * productInStocks; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "productInfo") public Set
	 * getInvoices() { return this.invoices; }
	 * 
	 * public void setInvoices(Set invoices) { this.invoices = invoices; }
	 */

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public Integer getCateId() {
		return cateId;
	}

	public void setCateId(Integer cateId) {
		this.cateId = cateId;
	}
	
	
	
}




