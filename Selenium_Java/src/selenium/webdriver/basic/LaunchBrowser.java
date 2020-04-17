package selenium.webdriver.basic;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.ConnectException;

public class LaunchBrowser {
	
	//DisplayCandidates.aspx and CreateInvestigation.aspx

	public static WebDriver driver = null;
	
	public static final String input_file = "C:\\Users\\sabar\\Downloads\\duns\\04142020_dunslookup.txt";
	public static final String output_file = "C:\\Users\\sabar\\Downloads\\duns\\04142020_dunslookup_output.txt";
	public static FileOutputStream fos;
	public static Writer out = null;
	public static BufferedReader inputStream = null;
	
	

	public static void main(String[] args) {

		try {
			System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");

			boolean newDriverNeeded = true;


			fos = new FileOutputStream(output_file);
			out = new OutputStreamWriter(fos, "utf8");
			String l, newline;
			int countofcols;
			int count = 0;
			int iter = 0;
			inputStream = new BufferedReader(new FileReader(input_file));


			while ((l = inputStream.readLine()) != null) {
				
				
				
				boolean lastColumnAppended = false;
				WebElement lookupAnother = null;
				//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

				try{
					if(newDriverNeeded) {
						System.out.println("New driver creating after old driver lost connection");
						driver = new ChromeDriver();
						count =0;
						while(count<5) {
							try {
								driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
								count += 5;
							}catch(Exception e) {
								e.printStackTrace();
								count++;
							}
						}
			
						driver.navigate().to("https://fdadunslookup.com/");
					
						String title = driver.getTitle();
			
						System.out.println("title of page is = " + title);
						System.out.println("driver = " + driver);
					
					}
					
					newDriverNeeded = false;
					
					
					if("https://fdadunslookup.com/".equals( driver.getCurrentUrl() ) || driver.getCurrentUrl().indexOf("Login.aspx") > -1 ) {

						count =0;

						while(count<5) {

							try {
								System.out.println("current url in tab = "+driver.getCurrentUrl());
								WebElement email = driver.findElement(By.id("MainContent_Txt_EmailID"));
								email.sendKeys("duns@registrarcorp.com");
								WebElement password = driver.findElement(By.id("MainContent_Txt_Password"));
								password.sendKeys("Sabar1sh$$");
								driver.findElement(By.id("MainContent_Btn_Login")).click();
								count += 5;
							}catch(Exception e) {
								e.printStackTrace();
								count++;
							}

						}

					}


					if(driver.getCurrentUrl().indexOf("Disclaimer.aspx") > -1) {

						count = 0;
						while(count < 5) {
							try {
								driver.findElement(By.id("MainContent_Btn_Enter")).click();
								count += 5;

							}catch(Exception e) {
								e.printStackTrace();
								count++;
							}

						}


					}

					newline = l.trim();
					String[] cols = newline.split("\t");
					countofcols = cols.length;
					int j = 0;
					for (String s : cols) {

						System.out.println("cols[" + j + "] =" + s);
						j++;
					}
					if (!"DUNS".equals(cols[2])) {
						
						if(driver.getCurrentUrl().indexOf("DUNSValidationOrLookup.aspx") > -1) {
					
						WebElement duns = null;
						

						WebElement name = null;
						WebElement address = null;
						WebElement clearFields = null;
						count = 0;
						String dunsNum = cols[2].replaceAll("[^\\d.]", "");
						if(dunsNum.length() < 9 && dunsNum.length() > 0)
							dunsNum = String.format("%0"+(9-dunsNum.length())+"d%s",0,dunsNum);
						else if(dunsNum.length() == 9)
							dunsNum = dunsNum;
						else
							dunsNum = "";



						while(count < 15) {
							try{
								duns = driver.findElement(By.name("ctl00$MainContent$Txt_DUNS"));
								if(! "".equals(dunsNum))
									sendChar(duns, dunsNum);
								count += 15;
							}catch(Exception e) {
								e.printStackTrace();
								System.out.println("Exception adding duns number..trying again");
								count += 1;
							}

						}

						String companyName = cols[1].replace("\"", "").replace("'", "\'").trim();
						System.out.println("efiing company name = " + companyName);




						JavascriptExecutor runJS = ((JavascriptExecutor) driver);
						try {
							name = driver.findElement(By.id("Txt_Name"));
							runJS.executeScript("arguments[0].value=\"" + companyName + "\";", name);

						}catch(Exception e) {

							System.out.println("moving on from exception in company name try block");
							e.printStackTrace();
						}
						// Actions performAct = new Actions(driver); performAct.sendKeys(name, companyName).build().perform();
						//name.sendKeys(companyName);

						// sendChar(name, companyName);
						String companyAddress = cols[3].replace("\"", "").trim() + " " + cols[4].replace("\"", "").trim();
						System.out.println("effing companyAddress = " + companyAddress);
						count = 0;
						while (count < 15) {
							try {

								address = driver.findElement(By.id("Txt_Address1"));
								address.click();
								//runJS.executeScript("arguments[0].value='" + companyAddress + "';", address);
								sendChar(address, companyAddress);
								count = count + 15;
							} catch (Exception e) {

								e.toString();
								System.out.println("Trying to recover from an exception 1 :" + e.getMessage());
								e.printStackTrace();
								count = count + 1;

							}

						}
						count =0;
						WebElement city =null;
						while(count < 15) {
							try {
								city = driver.findElement(By.name("ctl00$MainContent$Txt_City"));
								sendChar(city, cols[5].replaceAll("\"", "").replaceAll("-", " ").trim());
								count += 15;
							}catch(Exception e) {
								System.out.println("Exception entering city");
								count += 1;
							}

						}
						Select drpCountry = null;
						count = 0;
						while(count < 15) {
							try {	
								drpCountry =  new Select(driver.findElement(By.name("ctl00$MainContent$DropDown_Country")));
								if("UK".equals(cols[8].replace("\"", "").trim()))
									drpCountry.selectByValue("GB-UK");
								else
									drpCountry.selectByValue(cols[8].replace("\"", "").trim());
								System.out.println("drpCountry = " + drpCountry);
								count += 15;
							}catch(Exception e) {
								System.out.println("exception in finding country dropdown...trying again...");
								count +=1;
							}
						}


						count = 0;
						while(count < 15) {
							try {
								driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
								duns = driver.findElement(By.name("ctl00$MainContent$Txt_DUNS"));
								duns.click();
								count += 15;
							}catch(Exception e) {
								System.out.println("exception in finding country dropdown...trying again...");
								count +=1;
							}
						}



						//




						Select drpState = null;
						String stateDrpId = null;
						count =0;


						while(count < 5) {
							try {
								driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
								if("CN".equals(cols[8].replace("\"", "")))
									stateDrpId = "MainContent_DropDown_China_State";
								else
									stateDrpId = "MainContent_DropDown_"+cols[8].replace("\"", "").trim()+"_State";

								drpState = new Select(driver.findElement(By.id(stateDrpId)));
								count += 5;
							} catch (Exception e) {
								System.out.println("moving on from company state exception for dropdown");
								e.printStackTrace();
								count ++;
							}

						}


						if (drpState == null) {
							count =0;
							while(count < 5) {
								try {
									driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);			
									WebElement state = driver.findElement(By.name("ctl00$MainContent$Txt_State"));
									sendChar(state, cols[6].replaceAll("\"", "").trim());
									count += 5;
								}catch(Exception e) {
									System.out.println("exception : cannot access state text box...trying again");
									count += 1;
								}
							}

						} else {


							List<WebElement> allOptions = drpState.getOptions();

							for(WebElement option : allOptions) {
								String value = option.getAttribute("value");
								if(value.toLowerCase().indexOf(cols[6].replace("\"", "").toLowerCase().trim()) > -1 ) {
									drpState.selectByValue(value);
									break;
								}
							}


						}
						WebElement zipCode = null;


						count = 0;
						while(count < 15) {
							try {
								zipCode = driver.findElement(By.name("ctl00$MainContent$Txt_ZipCode"));
								sendChar(zipCode, cols[7].replaceAll("\\s", "").trim());
								count += 15;
							}catch(Exception e) {
								System.out.println("exception in finding country dropdown...trying again...");
								count +=1;
							}
						}
						//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
						count = 0;
						while(count < 5) {
							try {
								driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
								WebElement valid = driver.findElement(By.name("ctl00$MainContent$Btn_Validate"));
								valid.click();
								count += 5;
							}catch(Exception ex) {
								System.out.println("Exception in validation button click");
								ex.printStackTrace();
								count += 1;
							}

						}

						//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

						WebElement validation = null;
						

						count =0;
						while(count < 2) {
							try {
								validation = driver.findElement(By.id("MainContent_Label_Results"));
								count += 2;
							}catch(Exception e) {
								System.out.println("cannot find validation successful text..trying again");
								count += 1;
							}

						}

						try {


							if ("Validation Successful".equals(validation.getAttribute("title"))) {
								l = l.concat("\tYes");
								//out.write(l);
								System.out.println("Validation successful....looking up next company");
								lookupAnother = driver.findElement(By.name("ctl00$MainContent$Btn_Clear"));
								lookupAnother.click();

							}

						} catch (Exception e) {
							System.out.println("moving on from validation exception");
							e.printStackTrace();
						}


						
						//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

						if (validation == null) {

							l = l.concat("\tNo");
							//  out.write(l);



							count = 0;
							while(count<5) {
								try {
									driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
									clearFields = driver.findElement(By.name("ctl00$MainContent$Btn_Clear"));
									clearFields.click();
									if(!lastColumnAppended)
										l = l.concat("\tIncorrect Company Info");
									lastColumnAppended = true;
									count += 5;


								}catch(Exception e) {
									System.out.println("Invalid info entered");
									count++;

								}


							}
							
							
							}
						
						
						}



						if(driver.getCurrentUrl().indexOf("DisplayCandidates.aspx") > -1 || driver.getCurrentUrl().indexOf("CreateInvestigation.aspx") > -1 ) {

							try {
								// System.out.println("Validation failed....looking up next company");
								WebElement couldNotBeLocated = driver.findElement(By.id("MainContent_Label_Message"));
								if(couldNotBeLocated != null) {
									System.out.println("couldnotbelocated text = " + couldNotBeLocated.getAttribute("innerHTML"));
									if(couldNotBeLocated.getAttribute("innerHTML").indexOf("The information entered could not be located in the") > -1) {
										if(!lastColumnAppended)
											l = l.concat("\tNot found in DUNS database");
										lastColumnAppended = true;
										System.out.println("Data not found in DUNS");
										// out.write(l);
									}else {

										if(!lastColumnAppended)
											l=l.concat("\tDifferent from DUNS data");
										lastColumnAppended = true;
									}
								}



							}catch(Exception e) {
								System.out.println("Exception in couldNotBeLocated Block");
								e.printStackTrace();
								if(!lastColumnAppended)
									l=l.concat("\tDifferent from DUNS data");
								lastColumnAppended = true;
							}
							count = 0;

							while(count<5) {

								try {
									System.out.println("Validation failed....looking up next company");
									lookupAnother = driver.findElement(By.name("ctl00$MainContent$BtnBack"));
									lookupAnother.click();
									count += 5;
								}catch(Exception e) {

									System.out.println("Some exception when trying to go back to enter info again");
									e.printStackTrace();
									count++;
								}

							}
							
							
							}
						
						
						
						

					}

					l = l.concat("\n");
					out.write(l);



				}catch (ConnectException exception) {
		           
		        }catch(Exception exx){
		        	System.out.println("Driver connection exception " + exx);
			           System.out.println("Trying to create new driver");
			         //  driver.quit();
			           driver=null;
			           newDriverNeeded = true;
		        }

			}

		} catch (Exception e) {
			System.out.println("Exception 4");

			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}

		}

	}

	public static void sendChar(WebElement element, String value) {
		element.clear();

		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			String s = new StringBuilder().append(c).toString();
			element.sendKeys(s);
		}
	}

}
