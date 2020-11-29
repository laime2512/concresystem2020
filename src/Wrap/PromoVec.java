package Wrap;

import java.util.Arrays;

public class PromoVec {
	private Long codPromoVec[];
	private Long codProVec[];
	private Float pDescuentoVec[];
	public Long[] getCodPromoVec() {
		return codPromoVec;
	}
	public void setCodPromoVec(Long[] codPromoVec) {
		this.codPromoVec = codPromoVec;
	}
	public Long[] getCodProVec() {
		return codProVec;
	}
	public void setCodProVec(Long[] codProVec) {
		this.codProVec = codProVec;
	}
	public Float[] getpDescuentoVec() {
		return pDescuentoVec;
	}
	public void setpDescuentoVec(Float[] pDescuentoVec) {
		this.pDescuentoVec = pDescuentoVec;
	}
	@Override
	public String toString() {
		return "PromoVec [codPromoVec=" + Arrays.toString(codPromoVec) + ", codProVec=" + Arrays.toString(codProVec)
				+ ", pDescuentoVec=" + Arrays.toString(pDescuentoVec) + "]";
	}
}
