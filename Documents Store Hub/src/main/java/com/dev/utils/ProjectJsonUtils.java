package com.dev.utils;

public class ProjectJsonUtils {

	public enum DocumentCategory {

		TENTH_TH_MAKRSHEET("10th Marksheet"), TWELVE_TH_MAKRSHEET("12th Marksheet"), ADHAR("Adhar"), OTHER("Other");

		public final String documentCategory;

		DocumentCategory(String documentCategory) {
			this.documentCategory = documentCategory;
		}

		public static DocumentCategory fromString(String text) {
			for (DocumentCategory b : DocumentCategory.values()) {
				if (b.documentCategory.equalsIgnoreCase(text)) {
					return b;
				}
			}
			return null;
		}
	}

	public enum ImageExtensions {

		JPG("jpg"), JPEG("jpeg"), PNG("png");

		public final String ImgExtension;

		ImageExtensions(String ImgExtension) {
			this.ImgExtension = ImgExtension;
		}

		public static ImageExtensions fromString(String text) {
			for (ImageExtensions b : ImageExtensions.values()) {
				if (b.ImgExtension.equalsIgnoreCase(text)) {
					return b;
				}
			}
			return null;
		}
	}

}
