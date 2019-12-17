package inventory.model;

public class Paging //làm model phân trang
{

	private long totalRows; 
	private int totalPages; // tổng số page
	private int indexPage; //trang hiện tại
	private int recordPerPage=10; //tổng số bản ghi trên mỗi 1 trang là 10
	private int offset; // số thứ tự query của mỗi trang ,0-10,11-20,21-30
	
	public Paging(int recordPerPage)
	{
		this.recordPerPage = recordPerPage;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		if(totalRows >0) //nếu có nhiều hơn 1 bản ghi
		{
			totalPages =(int) Math.ceil(totalRows/(double)recordPerPage); // ví dụ có 20 bản ghi nó sự tự chia thành 2 bản mỗi bản 10-10
		}
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(int indexPage) {
		this.indexPage = indexPage;
	}

	public int getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public int getOffset() {
		if(indexPage>0)
		{
			offset = indexPage*recordPerPage -recordPerPage; // tính toán vị trị hiện offset bắt đầu từ bao nhiêu từ 0 10 20 ... ,1*10-10=0 ,2*10-10=10
		}
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
