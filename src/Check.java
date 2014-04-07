import javax.swing.JOptionPane;;
public class Check {
	public static String UpKey(String key){
		//Initialize Variables
		int len;
		String[] UKeyar;
		String Ukey = "";
		String TMPk;
		boolean haserror = false;
		int pos = 0;
		
		//Determine Length
		len = key.length() -1;
		UKeyar = new String[len + 1];
		
		//Convert To upper case
		TMPk = key.toUpperCase();
		
		//Move Into array, Filter for non alphabetic characters
		for (int i = 0; i <= len; i++){
			if (TMPk.charAt(i) >= 65){
				if (TMPk.charAt(i) <= 90){
					UKeyar[pos] = Character.toString(TMPk.charAt(i));
					pos++;
				}
				else{
					haserror = true;
				}	
			}
			else{
				haserror = true;
			}
		}
		
		//report errors
		if (haserror == true){
			Error(0);
		}
		//Convert array back to string
		for(int i = 0; i <= UKeyar.length - 1; i++){
			if (UKeyar[i] != null){
				Ukey = Ukey + UKeyar[i].toString();
			}
		}
		
		//Check lengths
		if (Ukey.length() >= 5){
			//Return String
			return Ukey;
			
		}
		else{
			//Return Error
			Error(1);
			return Ukey = "error";
		}
		
	}
	public static void Error(int num){
		if (num == 0){
			JOptionPane.showMessageDialog(null, "Incorrect Charecter Entry");
			Interface.inter.indicator.setText("Idle");
		}
		if (num == 1){
			JOptionPane.showMessageDialog(null, "Key Length Requirement Not Met");
			Interface.inter.indicator.setText("Idle");
		}
		
	}
}


