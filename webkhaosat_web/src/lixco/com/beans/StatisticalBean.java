package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lixco.com.entities.Question;
import lixco.com.entities.Rating;
import lixco.com.entities.RatingQuantity;
import lixco.com.entities.Statistical;
import lixco.com.entities.StatisticalEnd;
import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.servicepublic.EmployeeDTO;

@ManagedBean
@ViewScoped
public class StatisticalBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Statistical> statisticalBegin;
	private List<StatisticalEnd> statisticalEndFirst;
	private List<StatisticalEnd> statisticalEndSecond;
	private Long surveyId;
	private Survey surveyPlaying;

	private List<EmployeeDTO> employeeBySurListDTO;
	private EmployeeDTO[] employeeBySurDTO;
	private static ObjectMapper mapper = new ObjectMapper();
	private int toanboKS = 0;
	private int soluongdaKS = 0;
	private List<User_Result> listDaKhaoSat;
	private List<User_Result> listChuaHoanThanhKhaoSat;
	DepartmentServicePublic departmentServicePublic;

	@Override
	protected void initItem() {
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		// statisticalBegin = USER_RESULT_SERVICE.getStatistical(surveyId);
		// statisticalEndFirst = getListStatisticalBegin(surveyId);
		// statisticalEndSecond = castListStatisticalEnd(statisticalBegin,
		// statisticalEndFirst);
		// soluongdaKS = USER_RESULT_SERVICE.findByCountResult(surveyId);
		soluongdaKS = USER_RESULT_SERVICE.find(surveyId, null, null).size();
		departmentServicePublic = new DepartmentServicePublicProxy();

		// Thong ke so lieu
		try {
			if (StringUtils.isEmpty(surveyPlaying.getUsersJson())) {
				employeeBySurListDTO = new ArrayList<EmployeeDTO>();
			} else {
				employeeBySurDTO = mapper.readValue(surveyPlaying.getUsersJson(), EmployeeDTO[].class);
				employeeBySurListDTO = new ArrayList<>();
				employeeBySurListDTO = Arrays.asList(employeeBySurDTO);
				toanboKS = employeeBySurListDTO.size();
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	// public void danhSachChuaHoanThanh() throws IOException {
	// listDaKhaoSat = USER_RESULT_SERVICE.findByResult(surveyId, null);
	// List<User_Result> listChuaHoanThanh = new ArrayList<>();
	// for(int i = 0; i < listDaKhaoSat.size(); i++) {
	// boolean check = false;
	// for(int j = 0; j < employeeBySurListDTO.size(); j++) {
	// if(employeeBySurListDTO.get(j).getCode().equals(listDaKhaoSat.get(i).getEmployeeCode()))
	// {
	// check =true;
	// break;
	// }
	// }
	// if(check == false) {
	// listChuaHoanThanh.add(listDaKhaoSat.get(i));
	// }
	// }
	//
	//
	// XSSFWorkbook workbook = new XSSFWorkbook();
	// XSSFSheet sheet = null;
	// sheet = workbook.createSheet("DS NV Chưa hoàn thành KS");
	// int rownum = 0;
	// Cell cell;
	// Row row;
	// XSSFCellStyle style = createStyleForTitle(workbook);
	// style.setAlignment(CellStyle.ALIGN_CENTER);
	//
	// CellStyle styleContent = workbook.createCellStyle();
	// row = sheet.createRow(rownum);
	//
	//// // EmpNo
	// cell = row.createCell(0);
	// cell.setCellValue("Mã NV");
	// // xep loai// EmpName
	// cell = row.createCell(1);
	// cell.setCellValue("Tên NV");
	// cell.setCellStyle(style);
	// // Salary
	// cell = row.createCell(2);
	// cell.setCellValue("Phòng ban");
	// cell.setCellStyle(style);
	////
	//
	//// Data
	// for (User_Result kq : listChuaHoanThanh) {
	// Gson gson = new Gson();
	// rownum++;
	// row = sheet.createRow(rownum);
	//
	// // EmpNo (A)
	// cell = row.createCell(0);
	// cell.setCellValue(kq.getEmployeeCode());
	// // EmpName (B)
	// cell = row.createCell(1);
	// cell.setCellValue(kq.getEmployeeName());
	// // phong
	// cell = row.createCell(2);
	// cell.setCellValue(kq.getDepartmentName());
	// // ten nhan vien
	//
	// }
	//
	// String filename = "NVChuahoanthanh.xlsx";
	// FacesContext facesContext = FacesContext.getCurrentInstance();
	// ExternalContext externalContext = facesContext.getExternalContext();
	// externalContext.setResponseContentType("application/vnd.ms-excel");
	// externalContext.setResponseHeader("Content-Disposition", "attachment;
	// filename=\"" + filename + "\"");
	// workbook.write(externalContext.getResponseOutputStream());
	// // cancel progress
	// facesContext.responseComplete();
	// }

	public void danhSachChuaHoanThanh() {
		try {
			listDaKhaoSat = USER_RESULT_SERVICE.findByResult(surveyId, null);
			List<EmployeeDTO> listChuaHoanThanh = new ArrayList<>();
			for (int i = 0; i < employeeBySurListDTO.size(); i++) {
				if (employeeBySurListDTO.get(i).getName() != null) {
					boolean check = false;
					for (int j = 0; j < listDaKhaoSat.size(); j++) {
						if (employeeBySurListDTO.get(i).getCode().equals(listDaKhaoSat.get(j).getEmployeeCode())) {
							check = true;
							break;
						}
					}
					if (check == false) {
						listChuaHoanThanh.add(employeeBySurListDTO.get(i));
					}
				}
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = null;
			sheet = workbook.createSheet("DS NV Chưa hoàn thành KS");
			int rownum = 0;
			Cell cell;
			Row row;
			XSSFCellStyle style = createStyleForTitle(workbook);
			style.setAlignment(CellStyle.ALIGN_CENTER);

			row = sheet.createRow(rownum);

			// // EmpNo
			cell = row.createCell(0);
			cell.setCellValue("Mã NV");
			// xep loai// EmpName
			cell = row.createCell(1);
			cell.setCellValue("Tên NV");
			cell.setCellStyle(style);
			// Salary
			cell = row.createCell(2);
			cell.setCellValue("Phòng ban");
			cell.setCellStyle(style);
			//

			// Data
			for (EmployeeDTO kq : listChuaHoanThanh) {
				if (!kq.getCode().equals("0006768")) {
					rownum++;
					row = sheet.createRow(rownum);

					// EmpNo (A)
					cell = row.createCell(0);
					cell.setCellValue(kq.getCode());
					// EmpName (B)
					cell = row.createCell(1);
					cell.setCellValue(kq.getName());
					// phong
					// xu ly phong theo du lieu trung tam moi
					cell = row.createCell(2);
					Department dep = departmentServicePublic.findByCode("code", kq.getCodeDepart());
					if (dep != null) {
						if (dep.getLevelDep().getLevel() == 2) {
							cell.setCellValue(dep.getName());
						}
						if (dep.getLevelDep().getLevel() == 3) {
							cell.setCellValue(dep.getDepartment().getName());
						}
					} else {
						cell.setCellValue(kq.getNameDepart());
					}
				}
			}

			String filename = "NVChuahoanthanh.xlsx";
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			externalContext.setResponseContentType("application/vnd.ms-excel");
			externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			workbook.write(externalContext.getResponseOutputStream());
			// cancel progress
			facesContext.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Tao bo cau hoi
	// public List<StatisticalEnd> getListStatisticalBegin(long surveyId) {
	// List<StatisticalEnd> sts = new ArrayList<>();
	// List<Question> quests = QUESTION_SERVICE.find(null, surveyId, null);
	// for (Question q : quests) {
	// if (q.getQuestionType().getId() != 1 && q.getQuestionType().getId() != 4)
	// {
	// StatisticalEnd stNew = new StatisticalEnd();
	// stNew.setQuestion(q);
	// List<Rating> rts = RATING_SERVICE.find(q.getId(), 0L);
	// List<RatingQuantity> raQuas = new ArrayList<>();
	// for (Rating r : rts) {
	// RatingQuantity raQuaTemp = new RatingQuantity();
	// raQuaTemp.setRating(r);
	// raQuaTemp.setQuantity(0L);
	// raQuas.add(raQuaTemp);
	// }
	// stNew.setRatingQuantities(raQuas);
	// sts.add(stNew);
	// }
	// }
	// return sts;
	// }

	// Tao ra danh sach bo thong ke
	// public List<StatisticalEnd> castListStatisticalEnd(List<Statistical>
	// statisticals, List<StatisticalEnd> sts) {
	//
	// for (int i = 0; i < sts.size(); i++) {
	// for (int j = 0; j < sts.get(i).getRatingQuantities().size(); j++) {
	// for (int g = 0; g < statisticals.size(); g++) {
	// if (statisticals.get(g).getQuestionId() ==
	// sts.get(i).getQuestion().getId()) {
	// if (sts.get(i).getRatingQuantities().get(j).getRating().getName()
	// .equals(statisticals.get(g).getResult())) {
	// sts.get(i).getRatingQuantities().get(j).setQuantity(statisticals.get(g).getQuantity());
	// }
	// }
	// }
	// }
	// }
	// return sts;
	// }

	@Override
	protected Logger getLogger() {
		return null;
	}
	// GET AND SET

	public List<Statistical> getStatisticalBegin() {
		return statisticalBegin;
	}

	public void setStatisticalBegin(List<Statistical> statisticalBegin) {
		this.statisticalBegin = statisticalBegin;
	}

	public List<StatisticalEnd> getStatisticalEndFirst() {
		return statisticalEndFirst;
	}

	public void setStatisticalEndFirst(List<StatisticalEnd> statisticalEndFirst) {
		this.statisticalEndFirst = statisticalEndFirst;
	}

	public List<StatisticalEnd> getStatisticalEndSecond() {
		return statisticalEndSecond;
	}

	public void setStatisticalEndSecond(List<StatisticalEnd> statisticalEndSecond) {
		this.statisticalEndSecond = statisticalEndSecond;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public int getToanboKS() {
		return toanboKS;
	}

	public void setToanboKS(int toanboKS) {
		this.toanboKS = toanboKS;
	}

	public int getSoluongdaKS() {
		return soluongdaKS;
	}

	public void setSoluongdaKS(int soluongdaKS) {
		this.soluongdaKS = soluongdaKS;
	}
}
