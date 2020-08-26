package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;

@ManagedBean
@ViewScoped
public class BaoCaoBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Survey> allSurvey;
	private Survey surveySelected;
	private String loaiBaoCao;

	@Override
	protected void initItem() {
		surveySelected = new Survey();
		allSurvey = SURVEY_SERVICE.findAll();
		if (allSurvey.isEmpty() || allSurvey == null) {
			allSurvey = new ArrayList<Survey>();
		}
	}

	private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	public void baoCaoNangLucCanDaoTao() throws IOException {
//		PrimeFaces.current().executeScript("start().click()");
		if (loaiBaoCao.equals("")) {
			loaiBaoCao = "KYK";
		}
		// handle data
		// neu khong chon ky khao sat nao
		if (surveySelected == null || surveySelected.getName() == null) {
			surveySelected = allSurvey.get(0);
		}
		// khong y kien
		List<User_Result> listKetQua = new ArrayList<>();
		if (loaiBaoCao.equals("YK")) {
			listKetQua = USER_RESULT_SERVICE.findByResult(surveySelected.getId(), "Ý kiến khác");
		}
		// Tuyen duong
		if (loaiBaoCao.equals("KYK")) {
			listKetQua = USER_RESULT_SERVICE.findByResult(surveySelected.getId(), null);
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = null;
		if (loaiBaoCao.equals("KYK")) {
			sheet = workbook.createSheet("Báo cáo toàn bộ");
		}
		if (loaiBaoCao.equals("YK")) {
			sheet = workbook.createSheet("Báo cáo ý kiến khác");
		}

		int rownum = 0;
		Cell cell;
		Row row;
		XSSFCellStyle style = createStyleForTitle(workbook);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		CellStyle styleContent = workbook.createCellStyle();
		row = sheet.createRow(rownum);

//		// EmpNo
//		cell = row.createCell(0);
//		cell.setCellValue("Mã NV");
//		// xep loai// EmpName
//		cell = row.createCell(1);
//		cell.setCellValue("Tên NV");
//		cell.setCellStyle(style);
//		// Salary
//		cell = row.createCell(2);
//		cell.setCellValue("Phòng ban");
//		cell.setCellStyle(style);
//		
		
		// Grade
		cell = row.createCell(0);
		cell.setCellValue("Kỳ khảo sát");
		cell.setCellStyle(style);
		// Bonus
		cell = row.createCell(1);
		cell.setCellValue("Câu hỏi");
		cell.setCellStyle(style);
		// xep loai
		cell = row.createCell(2);
		cell.setCellValue("Kết quả");
		cell.setCellStyle(style);

		cell = row.createCell(3);
		cell.setCellValue("Ý kiến");
		cell.setCellStyle(style);
//		 Data
		for (User_Result kq : listKetQua) {
			Gson gson = new Gson();
			rownum++;
			row = sheet.createRow(rownum);
			
//			// EmpNo (A)
//			cell = row.createCell(0);
//			cell.setCellValue(kq.getEmployeeCode());
//			// EmpName (B)
//			cell = row.createCell(1);
//			cell.setCellValue(kq.getEmployeeName());
//			// phong
//			cell = row.createCell(2);
//			cell.setCellValue(kq.getDepartmentName());
//			// ten nhan vien
			
			
			cell = row.createCell(0);
			cell.setCellValue(kq.getQuestion().getSurvey().getName());

			cell = row.createCell(1);
			cell.setCellValue(kq.getQuestion().getName());
			// Nhom nang luc
			cell = row.createCell(2);
			cell.setCellValue(kq.getResult());

			if (kq.getNote() != null) {
				cell = row.createCell(3);
				cell.setCellValue(kq.getNote());
			}
		}

		String filename = "baocao.xlsx";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		externalContext.setResponseContentType("application/vnd.ms-excel");
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		workbook.write(externalContext.getResponseOutputStream());
		// cancel progress
		facesContext.responseComplete();
	}

//	public void ajaxLoaiKyKhaoSat() {
//		if (surveySelected != null) {
//			try {
//				kyDanhGiaNangLucs = kyDanhGiaService.findRange(loaiKyDanhGiaNangLuc);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<Survey> getAllSurvey() {
		return allSurvey;
	}

	public void setAllSurvey(List<Survey> allSurvey) {
		this.allSurvey = allSurvey;
	}

	public Survey getSurveySelected() {
		return surveySelected;
	}

	public void setSurveySelected(Survey surveySelected) {
		this.surveySelected = surveySelected;
	}

	public String getLoaiBaoCao() {
		return loaiBaoCao;
	}

	public void setLoaiBaoCao(String loaiBaoCao) {
		this.loaiBaoCao = loaiBaoCao;
	}
}
