import java.util.ArrayList;

import javax.xml.ws.handler.HandlerResolver;

public class Controller {
	static int roleSkinIndex = 1;
	static int commonAttackSkillGroupIndex = 27;
	static int cvIndex = 11;
	static int cvMuseumIndex = 1;
	static int equipSkillIndex = 18;
	static int skillIdIndex = 1;
	static int effectIdIndex = 6;
	static int skillShowSkillIndex = 8;
	static int skillShowEffectIndex = 22;
	static int crystalSkillGroupIndex = 21;
	static int ringSkillGroupIndex = 1;
	
	
	public Controller() {
		
	}
	
	public static boolean move(String srcVersion, String dstVersion, String Key, String basePathPre) {
		//String srcVersion = "jp_1_0";
		//String dstVersion = "jp_1_1";
		String srcLanguage = "chs";
		String dstLanguage = "chs";
		
		if(srcVersion.equals("jp_1_0") || srcVersion.equals("jp_1_1")) {
			srcLanguage = "jpn";
		}
		if(srcVersion.equals("us_1_0") || srcVersion.equals("us_2_0")) {
			srcLanguage = "eng";
		}
		
		if(dstVersion.equals("jp_1_0") || dstVersion.equals("jp_1_1")) {
			dstLanguage = "jpn";
		}
		if(dstVersion.equals("us_1_0") || dstVersion.equals("us_2_0")) {
			dstLanguage = "eng";
		}
		
		if(srcVersion.equals("tw_1_1")) {
			commonAttackSkillGroupIndex = 28;
			crystalSkillGroupIndex = 20;
		}
		
		if(srcVersion.equals("0_5")) {
			roleSkinIndex = 3;
			commonAttackSkillGroupIndex = 30;
			cvIndex = 18;
			cvMuseumIndex = 1;
			skillIdIndex = 2;
			effectIdIndex = 7;
			skillShowSkillIndex = 9;
			skillShowEffectIndex = 23;
			ringSkillGroupIndex = 2;
		}
		
		//String Key = "阳炎";
		
		String basePath = basePathPre + "\\app_";
		
		String itemXml = "\\data\\pick\\item.xml";
		String itemWorkSheet = "道具|item";
		String itemBaseKey = "英雄之证·";
		
		//System.out.println(basePath + " :" + itemBaseKey + Key);
		
		XmlController item = new XmlController(basePath + srcVersion + itemXml, basePath + dstVersion + itemXml, itemWorkSheet, itemBaseKey + Key, "21000", "24000", null, null);
		item.copyRow();
		
		String itemWorkSheet2 = "装备|equit";
		String itemTailKey = "戒指";
		XmlController item2 = new XmlController(basePath + srcVersion + itemXml, basePath + dstVersion + itemXml, itemWorkSheet2, Key + itemTailKey, "274050", "275000", null, null);
		item2.copyRow();
		
		if(!srcVersion.equals("0_5")) {
			String srcItemLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\pick_item.xml";
			String dstItemLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\pick_item.xml";
			XmlController itemLanguage = new XmlController(basePath + srcVersion + srcItemLanguageXml, basePath + dstVersion + dstItemLanguageXml, itemWorkSheet, item.getReturnKeyContent(), "21000", "24000", null, null);
			itemLanguage.copyRow();
			
			XmlController itemLanguage2 = new XmlController(basePath + srcVersion + srcItemLanguageXml, basePath + dstVersion + dstItemLanguageXml, itemWorkSheet2, item2.getReturnKeyContent(), "274050", "275000", null, null);
			itemLanguage2.copyRow();
		}
		
		ArrayList<String> itemArtList = item.getReturnArtPath();
		for(int i = 0; i < itemArtList.size(); i++) {
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + itemArtList.get(i).replaceAll("/", "\\\\");
			//System.out.println(artSrcPath);
			fileController.copyDir(artSrcPath);
		}
		
		ArrayList<String> itemArtList2 = item2.getReturnArtPath();
		for(int i = 0; i < itemArtList2.size(); i++) {
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + itemArtList2.get(i).replaceAll("/", "\\\\");
			//System.out.println(artSrcPath);
			fileController.copyDir(artSrcPath);
		}
		
		
		String heroXml = "\\data\\hero\\hero.xml";
		String heroWorkSheet = "英雄|heros";
		int[] specialIndexByHero = {roleSkinIndex, commonAttackSkillGroupIndex};
		XmlController hero = new XmlController(basePath + srcVersion + heroXml, basePath + dstVersion + heroXml, heroWorkSheet, Key, "10000", "20000", specialIndexByHero, null);
		hero.copyRow();
		ArrayList<String> heroContent = hero.getReturnContentByIndex();
		String roleSkinContent = "";
		if(heroContent.size() > 0) {
			roleSkinContent = heroContent.get(0);
		}
		String commonAttackSkillGroupContent = heroContent.get(1);
		
		String heroWorkSheet2 = "英雄信息|hero_note";
		int[] specialIndexByHero2 = {cvIndex};
		System.out.println("英雄ID: " + hero.getReturnKeyContent());
		XmlController hero2 = new XmlController(basePath + srcVersion + heroXml, basePath + dstVersion + heroXml, heroWorkSheet2, hero.getReturnKeyContent(), "10000", "20000", specialIndexByHero2, "0");
		hero2.copyRow();
		if(hero2.getReturnContentByIndex().get(0) == null) {
			System.out.println("cvContent is null");
		}
		String cvContent = hero2.getReturnContentByIndex().get(0);
		
		if(!srcVersion.equals("0_5")) {
			String srcHeroLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\hero_hero.xml";
			String dstHeroLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\hero_hero.xml";
			XmlController heroLanguage = new XmlController(basePath + srcVersion + srcHeroLanguageXml, basePath + dstVersion + dstHeroLanguageXml, heroWorkSheet, hero.getReturnKeyContent(), "10000", "20000", null, "0");
			heroLanguage.copyRow();
			
			XmlController heroLanguage2 = new XmlController(basePath + srcVersion + srcHeroLanguageXml, basePath + dstVersion + dstHeroLanguageXml, heroWorkSheet2, hero.getReturnKeyContent(), "10000", "20000", null, "0");
			heroLanguage2.copyRow();
		}
		
		String wakeXml = "\\data\\hero\\wake.xml";
		XmlController wake = new XmlController(basePath + srcVersion + wakeXml, basePath + dstVersion + wakeXml, heroWorkSheet, hero.getReturnKeyContent(), "10000", "20000", null, null);
		wake.copyRow();
		
		String surmountXml = "\\data\\hero\\surmount.xml";
		XmlController surmount = new XmlController(basePath + srcVersion + surmountXml, basePath + dstVersion + surmountXml, heroWorkSheet, hero.getReturnKeyContent(), "10000", "20000", null, null);
		surmount.copyRow();
		
		String revelationExpXml = "\\data\\hero\\revelation_exp.xml";
		String revelationExpSheet = "成长|grow";
		XmlController revelationExp = new XmlController(basePath + srcVersion + revelationExpXml, basePath + dstVersion + revelationExpXml, revelationExpSheet, hero.getReturnKeyContent(), "10000", "20000", null, null);
		revelationExp.copyRow();
		
		String cvXml = "\\data\\hero\\cv.xml";
		String cvWorkSheet = "角色配音组表|voice";
		XmlController cv = new XmlController(basePath + srcVersion + cvXml, basePath + dstVersion + cvXml, cvWorkSheet, cvContent, "0", "10000", null, null);
		cv.copyRow();
		
		ArrayList<String> cvArtList = cv.getReturnArtPath();
		if(!dstVersion.equals("jp_1_0") && cvArtList.size() > 0) {
			String cvArtPath = cvArtList.get(0);
			int lastIndex = cvArtPath.lastIndexOf("/");
			//System.out.println(lastIndex);
			cvArtPath = cvArtPath.substring(0, lastIndex);
			//System.out.println(cvArtPath);
			FileController cvFileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + cvArtPath.replaceAll("/", "\\\\");
			cvFileController.copyDir(artSrcPath);
		}
		
		
		String cvMuseumXml = "\\data\\hero\\cv_museum.xml";
		String cvWorkSheet2 = "角色配音组表|voice_id";
		int[] specialIndexByCv = {cvMuseumIndex};
		XmlController cvMuseum = new XmlController(basePath + srcVersion + cvMuseumXml, basePath + dstVersion + cvMuseumXml, cvWorkSheet2, hero.getReturnKeyContent(), "10000", "20000", specialIndexByCv, null);
		cvMuseum.copyRow();
		String cvMesumContent = "";
		if(cvMuseum.getReturnContentByIndex() == null || cvMuseum.getReturnContentByIndex().size() == 0) {
			System.out.println("cvMuseumContent is null");
		} else {
			cvMesumContent = cvMuseum.getReturnContentByIndex().get(0);
		}
		String[] cvMesumKeyContent = cvMesumContent.split(",");
		for(int i = 0; i < cvMesumKeyContent.length; i++) {
			cvMesumKeyContent[i] =  cvMesumKeyContent[i].replaceAll(" ", "");
			XmlController cvMesum2Temp = new XmlController(basePath + srcVersion + cvMuseumXml, basePath + dstVersion + cvMuseumXml, cvWorkSheet, cvMesumKeyContent[i], "0", "99999999", null, null);
			cvMesum2Temp.copyRow();
		}
		
		if(!srcVersion.equals("0_5") && !srcVersion.equals("jp_1_0")) {
			String srcCvMuseumLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\hero_cv_museum.xml";
			String dstCvMuseumLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\hero_cv_museum.xml";
			for(int i = 0; i < cvMesumKeyContent.length; i++) {
				XmlController cvMuseumLanguage = new XmlController(basePath + srcVersion + srcCvMuseumLanguageXml, basePath + dstVersion + dstCvMuseumLanguageXml, cvWorkSheet, cvMesumKeyContent[i], "0", "99999999", null, null);
				cvMuseumLanguage.copyRow();
			}
		}
		
		String heroRevelationAwardXml = "\\data\\hero\\hero_revelation_award.xml";
		String heroRevelationAwardWorkSheet = "天启奖励|award";
		XmlController heroRevelationAward = new XmlController(basePath + srcVersion + heroRevelationAwardXml, basePath + dstVersion + heroRevelationAwardXml, heroRevelationAwardWorkSheet, hero.getReturnKeyContent(), "10000", "20000", null, null);
		heroRevelationAward.copyMultiRowsByRevelationAward();
		
		String heroRevelationAwardWorkSheet2 = "天启激活|cv";
		XmlController heroRevelationAward2 = new XmlController(basePath + srcVersion + heroRevelationAwardXml, basePath + dstVersion + heroRevelationAwardXml, heroRevelationAwardWorkSheet2, hero.getReturnKeyContent(), "10000", "20000", null, null);
		heroRevelationAward2.copyRow();
		
		if(!srcVersion.equals("0_5")) {
			String srcHeroRevelationAwardLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\hero_hero_revelation_award.xml";
			String dstHeroRevelationAwardLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\hero_hero_revelation_award.xml";
			XmlController heroRevelationAwardLanguage = new XmlController(basePath + srcVersion + srcHeroRevelationAwardLanguageXml, basePath + dstVersion + dstHeroRevelationAwardLanguageXml, heroRevelationAwardWorkSheet2, hero.getReturnKeyContent(), "10000", "20000", null, null);
			heroRevelationAwardLanguage.copyRow();
		}
		
		String roleHeadXml = "\\data\\player\\role_head.xml";
		String roleHeadWorkSheet = "角色头像|head";
		if(itemArtList.size() == 0) {
			System.out.println("no head!");
			return false;
		}
		XmlController roleHead = new XmlController(basePath + srcVersion + roleHeadXml, basePath + dstVersion + roleHeadXml, roleHeadWorkSheet, itemArtList.get(0), "1000", "10000", null, null);
		roleHead.copyRow();
		
		String roleSkinXml = "\\data\\common\\roleskin.xml";
		String roleSkinWorkSheet = "英雄外观|heroskin";
		XmlController roleSkin = new XmlController(basePath + srcVersion + roleSkinXml, basePath + dstVersion + roleSkinXml, roleSkinWorkSheet, roleSkinContent, "1000", "10000", null, null);
		roleSkin.copyRow();
		
		ArrayList<String> roleSkinArtList = roleSkin.getReturnArtPath();
		for(int i = 0; i < roleSkinArtList.size(); i++) {
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + roleSkinArtList.get(i).replaceAll("/", "\\\\");
			if(i == 0) { //特殊处理第一个json文件（即动作文件）
				int lastIndex = artSrcPath.lastIndexOf("\\");
				//System.out.println(lastIndex);
				artSrcPath = artSrcPath.substring(0, lastIndex);
			}
			//System.out.println(artSrcPath);
			fileController.copyDir(artSrcPath);
		}
		
		String equipXml = "\\data\\equip\\equip.xml";
		String equipWorkSheet = "装备|equip";
		if(srcVersion.equals("tw_1_1")) {
			equipSkillIndex = 15;
		}
		int[] specialIndexByEquip = {equipSkillIndex};
		XmlController equip = new XmlController(basePath + srcVersion + equipXml, basePath + dstVersion + equipXml, equipWorkSheet, item2.getReturnKeyContent(), "274050", "275000", specialIndexByEquip, null);
		equip.copyRow();
		
		ArrayList<String> equipContent = equip.getReturnContentByIndex();
		String equipSkillContent = "";
		if(equipContent.size() > 0) {
			equipSkillContent = equipContent.get(0);
		}
		
		String equipWorkSheet2 = "属性解放|exproperty";
		XmlController equip2 = new XmlController(basePath + srcVersion + equipXml, basePath + dstVersion + equipXml, equipWorkSheet2, item2.getReturnKeyContent(), "274050", "275000", null, null);
		equip2.copyRow();
		
		String resonateXml = "\\data\\equip\\resonate.xml";
		XmlController resonate = new XmlController(basePath + srcVersion + resonateXml, basePath + dstVersion + resonateXml, equipWorkSheet, item2.getReturnKeyContent(), "274050", "275000", null, null);
		resonate.copyRow();
		
		String expropertyXml = "\\data\\equip\\exproperty.xml";
		String expropertyWorkSheet = "装备属性解放|equip_exproperty";
		XmlController exproperty = new XmlController(basePath + srcVersion + expropertyXml, basePath + dstVersion + expropertyXml, expropertyWorkSheet, item2.getReturnKeyContent(), "274050", "275000", null, null);
		//注意这里目标版本如果是kr_1_1的话，要手动更改一下，这里多了一列，暂时不想去XmlController里去特殊处理了。
		exproperty.copyRow();
		if(dstVersion.equals("kr_1_1")) {
			System.out.println("Warming!!  " + basePath + dstVersion + expropertyXml + " " +  expropertyXml + ": 要手动更改一下，这里多了一列");
		}
		
		ArrayList<String> ringSkillGroup = new ArrayList<>();
		
		String equipSkillXml = "\\data\\equip\\equip_skill.xml";
		String equipSkillWorkSheet = "装备技能|equip_skill";
		String[] equipSkillKeyContent = equipSkillContent.split(",");
		int[] specialIndexByEquipSkill = {ringSkillGroupIndex}; 
		for(int i = 0; i < equipSkillKeyContent.length; i++) {
			equipSkillKeyContent[i] =  equipSkillKeyContent[i].replaceAll(" ", "");
			XmlController equipSkill = new XmlController(basePath + srcVersion + equipSkillXml, basePath + dstVersion + equipSkillXml, equipSkillWorkSheet, equipSkillKeyContent[i], "274000", "275000", specialIndexByEquipSkill, null);
			equipSkill.copyRow();
			
			ArrayList<String> ringSkillGroupKeyContentList = equipSkill.getReturnContentByIndex();
			for(int j = 0; j < ringSkillGroupKeyContentList.size(); j++) {
				//System.out.println(ringSkillGroupKeyContentList.get(j));
				String[] skillGropStings = ringSkillGroupKeyContentList.get(j).split(",");
				for(int k = 0; k < skillGropStings.length; k++) {
					skillGropStings[k] = skillGropStings[k].replaceAll("\\}", "");
					skillGropStings[k] = skillGropStings[k].replaceAll("\\{", "");
					skillGropStings[k] = skillGropStings[k].replaceAll(" ", "");
					//System.out.println(skillGropStings[j]);
					if(XmlController.isNumber(skillGropStings[k]) && Integer.parseInt(skillGropStings[k]) >= 600000 && Integer.parseInt(skillGropStings[k]) <= 700000) {
						ringSkillGroup.add(skillGropStings[k]);
					}
				}
			}
		}
		
		String crystalXml = "\\data\\pick\\crystal.xml";
		String crystalWorkSheet = "宝石套装|crystalsuit";
		int[] specialIndexByCrystal = {crystalSkillGroupIndex}; 
		XmlController crystal = new XmlController(basePath + srcVersion + crystalXml, basePath + dstVersion + crystalXml, crystalWorkSheet, Key, "0", "100000", specialIndexByCrystal, hero.getReturnKeyContent());
		crystal.copyMultiRowsByCrystal();
		//System.out.println(crystal.getReturnContentByIndex());
		ArrayList<String> crystalSkillGroupKeyContentList = crystal.getReturnContentByIndex();
		
		
		ArrayList<String> crystalSkillGroup = new ArrayList<>();
		for(int i = 0; i < crystalSkillGroupKeyContentList.size(); i++) {
			String[] skillGropStings = crystalSkillGroupKeyContentList.get(i).split(",");
			for(int j = 0; j < skillGropStings.length; j++) {
				skillGropStings[j] = skillGropStings[j].replaceAll("\\}", "");
				skillGropStings[j] = skillGropStings[j].replaceAll("\\{", "");
				skillGropStings[j] = skillGropStings[j].replaceAll(" ", "");
				//System.out.println(skillGropStings[j]);
				if(XmlController.isNumber(skillGropStings[j]) && Integer.parseInt(skillGropStings[j]) >= 80000 && Integer.parseInt(skillGropStings[j]) <= 90000) {
					crystalSkillGroup.add(skillGropStings[j]);
				}
			}
		}
		
		
		ArrayList<String> crystalKeyContentList = crystal.getMultiReturnKeyContent();
		if(!srcVersion.equals("0_5")) {
			String srcCrystalLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\pick_crystal.xml";
			String dstCrystalLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\pick_crystal.xml";
			for(int i = 0; i < crystalKeyContentList.size(); i++) {
				XmlController crystalLanguage = new XmlController(basePath + srcVersion + srcCrystalLanguageXml, basePath + dstVersion + dstCrystalLanguageXml, crystalWorkSheet, crystalKeyContentList.get(i), "0", "100000", null, null);
				crystalLanguage.copyRow();
			}
		}
		
		String skillGroupXml = "\\data\\combat\\skill_xml\\skillgroup.xml";
		String skillGroupWorkSheet = "技能组|skillgroup";
		int[] specialIndexBySkillGroup = {skillIdIndex};
		System.out.println(commonAttackSkillGroupContent);
		XmlController skillGroup = new XmlController(basePath + srcVersion + skillGroupXml, basePath + dstVersion + skillGroupXml, skillGroupWorkSheet, commonAttackSkillGroupContent, "100000", "200000", specialIndexBySkillGroup, null);
		skillGroup.copyMultiRowsBySkillGroup();
		ArrayList<String> skillGroupArtList = skillGroup.getReturnArtPath();
		for(int i = 0; i < skillGroupArtList.size(); i++) {
			//System.out.println(skillGroupArtList.get(i));
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + skillGroupArtList.get(i).replaceAll("/", "\\\\");
			fileController.copyDir(artSrcPath);
		}
		ArrayList<String> skillGroupKeyContentList = skillGroup.getMultiReturnKeyContent();
		if(!srcVersion.equals("0_5")) {
			String srcSkillGroupLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\skill_xml_skillgroup.xml";
			String dstSkillGroupLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\skill_xml_skillgroup.xml";
			for(int i = 0; i < skillGroupKeyContentList.size(); i++) {
				XmlController skillGroupLanguage = new XmlController(basePath + srcVersion + srcSkillGroupLanguageXml, basePath + dstVersion + dstSkillGroupLanguageXml, skillGroupWorkSheet, skillGroupKeyContentList.get(i), "100000", "200000", null, null);
				skillGroupLanguage.copyRow();
			}
		}
		
		ArrayList<String> skillsId = skillGroup.getReturnContentByIndex();
		String skills1Xml = "\\data\\combat\\skill_xml\\skills1.xml";
		String skills1WorkSheet = "技能主表|skills1";
		int[] specialIndexBySkills1 = {effectIdIndex, skillShowSkillIndex};
		
		String effectXml = "\\data\\combat\\skill_xml\\effects.xml";
		String effectWorkSheet = "效果|effects";
		int[] specialIndexByEffect = {skillShowEffectIndex};
		
		
		String[] effectsIdList = null;
		String commonAttackSkillShow = "";
		String commonAttackEffectShow = "";
		ArrayList<String> skillSkillShow = new ArrayList<>();
		if(skillsId.size() >= 2) {
			String[] commonAttackSkillsIdList = skillsId.get(0).split("}");
			for(int i = 0; i < commonAttackSkillsIdList.length; i++) {
				String[] commonAttackSkillsIdList2 = commonAttackSkillsIdList[i].split("=");
				String commonAttackSkillsId = commonAttackSkillsIdList2[2].replaceAll(" ", "");
				XmlController commonAttackSkills = new XmlController(basePath + srcVersion + skills1Xml, basePath + dstVersion + skills1Xml, skills1WorkSheet, commonAttackSkillsId, "10000", "20000", specialIndexBySkills1, null);
				commonAttackSkills.copyRow();
				
				ArrayList<String> skillsReturnContentList = commonAttackSkills.getReturnContentByIndex();
				String[] CommonAttackEffectIdList =  skillsReturnContentList.get(0).replaceAll(" ", "").split(",");
				for(int j = 0; j < CommonAttackEffectIdList.length; j++) {
					XmlController commonAttakEffects = new XmlController(basePath + srcVersion + effectXml, basePath + dstVersion + effectXml, effectWorkSheet, CommonAttackEffectIdList[i], "10000", "20000", specialIndexByEffect, null);
					commonAttakEffects.copyRow();
					commonAttackEffectShow = commonAttakEffects.getReturnContentByIndex().get(0);
				}
				
				if(commonAttackSkills.getReturnContentByIndex().size() > 1) {
					commonAttackSkillShow = commonAttackSkills.getReturnContentByIndex().get(1);
				}
				
			}
			
			
			String[] skills1IdList = skillsId.get(1).split("}");
			for(int i = 0; i < skills1IdList.length; i++) {
				String[] skills1IdList2 = skills1IdList[i].split("=");
				String skills1Id = skills1IdList2[2].replaceAll(" ", "");
				XmlController skills = new XmlController(basePath + srcVersion + skills1Xml, basePath + dstVersion + skills1Xml, skills1WorkSheet, skills1Id, "30000", "50000", specialIndexBySkills1, null);
				skills.copyMultiRowsBySkills();
				
				ArrayList<String> effectsReturnContentList = skills.getReturnContentByIndex();
				effectsIdList = effectsReturnContentList.get(0).replaceAll(" ", "").split(",");
				
				for(int j = 0; j < effectsReturnContentList.size(); j++) {
					if(Math.floorMod(j, specialIndexBySkills1.length) == 1) {
						skillSkillShow.add(effectsReturnContentList.get(j));
					}
				}
			}
		}
		String formulasXml = "\\data\\combat\\skill_xml\\formulas.xml";
		String formulasWorkSheet = "技能数值|formulas";
		
		
		XmlController effect = null;
		XmlController formulas = null;
		if(effectsIdList != null && effectsIdList.length > 0 && XmlController.isNumber(effectsIdList[0])) {
			//int effectIdNumber = Integer.parseInt(effectsIdList[0]);
			effect = new XmlController(basePath + srcVersion + effectXml, basePath + dstVersion + effectXml, effectWorkSheet, effectsIdList[0], "30000", "50000", specialIndexByEffect, null);
			effect.copyMultiRowsBySkills();
			
			formulas = new XmlController(basePath + srcVersion + formulasXml, basePath + dstVersion + formulasXml, formulasWorkSheet, effectsIdList[0], "30000", "50000", null, null);
			formulas.CopyMultiRowsByFormulas();
			
		}
		
		
		
		
		String skillShowXml = "\\data\\combat\\skill_show.xml";
		String skillShowWorkSheet = "技能表现|skill_show";
		String skillShowWorkWheet2 = "效果表现|effect_show";
		
		XmlController skillShow = new XmlController(basePath + srcVersion + skillShowXml, basePath + dstVersion + skillShowXml, skillShowWorkSheet, commonAttackSkillShow, "10000", "20000", null, null);
		skillShow.copyRow();
		ArrayList<String> skillShowArtPath = skillShow.getReturnArtPath();
		for(int i = 0; i < skillShowArtPath.size(); i++) {
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + skillShowArtPath.get(i).replaceAll("/", "\\\\");
			fileController.copyDir(artSrcPath);
		}
		
		//System.out.println(commonAttackEffectShow);
		XmlController skillShow2 = new XmlController(basePath + srcVersion + skillShowXml, basePath + dstVersion + skillShowXml, skillShowWorkWheet2, commonAttackEffectShow, "0", "1000", null, "0");
		skillShow2.copyRow();
		ArrayList<String> skillShowEffectArtPath = skillShow2.getReturnArtPath();
		for(int i = 0; i < skillShowEffectArtPath.size(); i++) {
			FileController fileController = new FileController(srcVersion, dstVersion);
			String artSrcPath = basePath + srcVersion + "\\" + skillShowEffectArtPath.get(i).replaceAll("/", "\\\\");
			fileController.copyDir(artSrcPath);
		}
		
		for(int i = 0; i < skillSkillShow.size(); i++) {
			XmlController skillShow3 = new XmlController(basePath + srcVersion + skillShowXml, basePath + dstVersion + skillShowXml, skillShowWorkSheet, skillSkillShow.get(i), "30000", "50000", null, null);
			skillShow3.copyRow();
			ArrayList<String> skillShowSkillArtPath = skillShow3.getReturnArtPath();
			for(int j = 0; j < skillShowSkillArtPath.size(); j++) {
				//System.err.println(skillShowSkillArtPath.get(j));
				FileController fileController = new FileController(srcVersion, dstVersion);
				String artSrcPath = basePath + srcVersion + "\\" + skillShowSkillArtPath.get(j).replaceAll("/", "\\\\");
				int lastIndex = artSrcPath.lastIndexOf("\\");
				
				if(!artSrcPath.substring(lastIndex+1, artSrcPath.length()).contains(".")) {
					artSrcPath = artSrcPath.substring(0, lastIndex);
					//System.out.println(resStrings[k]);
				}
				//System.err.println(artSrcPath);
				fileController.copyDir(artSrcPath);
			}
		}
		
		if(effect != null) {
			ArrayList<String> effectSkillShow = effect.getReturnContentByIndex();
			for(int i = 0; i < effectSkillShow.size(); i++) {
				XmlController skillShow4 = new XmlController(basePath + srcVersion + skillShowXml, basePath + dstVersion + skillShowXml, skillShowWorkWheet2, effectSkillShow.get(i), "0", "10000", null, "0");
				skillShow4.copyRow();
				ArrayList<String> skillShowEffectArtPath2 = skillShow4.getReturnArtPath();
				for(int j = 0; j < skillShowEffectArtPath2.size(); j++) {
					//System.out.println(skillShowEffectArtPath2.get(j));
					
					String artSrcPath = skillShowEffectArtPath2.get(j);
					if(artSrcPath.contains("}")) {
						artSrcPath = artSrcPath.replaceAll("\\}", "");
						artSrcPath = artSrcPath.replaceAll("\\{", "");
						artSrcPath = artSrcPath.replaceAll("\"", "");
						artSrcPath = artSrcPath.replaceAll(" ", "");
						artSrcPath = artSrcPath.replaceAll("/", "\\\\");
						String[] resStrings = artSrcPath.split(",");
						for(int k = 0; k < resStrings.length; k++) {
							if(resStrings[k].contains("res\\")) {
								int lastIndex = resStrings[k].lastIndexOf("\\");
								if(!resStrings[k].substring(lastIndex+1, resStrings[k].length()).contains(".")) {
									resStrings[k] = resStrings[k].substring(0, lastIndex);
									//System.out.println(resStrings[k]);
								}
								artSrcPath = basePath + srcVersion + "\\" + resStrings[k];
								//System.err.println(artSrcPath);
								FileController fileController = new FileController(srcVersion, dstVersion);
								fileController.copyDir(artSrcPath);
							}
						}
					} else {
						artSrcPath = basePath + srcVersion + "\\" + artSrcPath.replaceAll("/", "\\\\");
						if(!artSrcPath.contains("res\\audio")) {
							//System.out.println(artSrcPath);
							int lastIndex = artSrcPath.lastIndexOf("\\");
							
							if(!artSrcPath.substring(lastIndex+1, artSrcPath.length()).contains(".")) {
								artSrcPath = artSrcPath.substring(0, lastIndex);
								//System.out.println(artSrcPath);
							}
							//System.err.println(artSrcPath);
							FileController fileController = new FileController(srcVersion, dstVersion);
							fileController.copyDir(artSrcPath);
						}
					}
				}
			}
		}
		
		
		
		String skills2Xml = "\\data\\combat\\skill_xml\\skills2.xml";
		String skills2WorkSheet = "装备技能主表|skills2";
		int[] specialIndexBySkills2 = {effectIdIndex};
		
		ArrayList<String> crystalSkillIds = new ArrayList<>();
		for(int i = 0; i < crystalSkillGroup.size(); i++) {
			//System.out.println("crystal SkillGroup: " + crystalSkillGroup.get(i));
			XmlController crystalSkillGroupXC = new XmlController(basePath + srcVersion + skillGroupXml, basePath + dstVersion + skillGroupXml, skillGroupWorkSheet, crystalSkillGroup.get(i), "80000", "90000", specialIndexBySkillGroup, null);
			crystalSkillGroupXC.copyMultiRowsByCrystalSkillGroup();

			if(!srcVersion.equals("0_5")) {
				String srcSkillGroupLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\skill_xml_skillgroup.xml";
				String dstSkillGroupLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\skill_xml_skillgroup.xml";
				XmlController crystalSkillGroupLanguage = new XmlController(basePath + srcVersion + srcSkillGroupLanguageXml, basePath + dstVersion + dstSkillGroupLanguageXml, skillGroupWorkSheet, crystalSkillGroup.get(i), "80000", "90000", null, null);
				crystalSkillGroupLanguage.copyRow();
			}
			
			for(int j = 0; j < crystalSkillGroupXC.getReturnContentByIndex().size(); j++) {
				if(!crystalSkillIds.contains(crystalSkillGroupXC.getReturnContentByIndex().get(j))) {
					crystalSkillIds.add(crystalSkillGroupXC.getReturnContentByIndex().get(j));
				}
			}
		}
		
		ArrayList<String> crystalEffectsReturnContentList = new ArrayList<>();
		ArrayList<String> crystalSkillIdList = new ArrayList<>();
		for(int i = 0; i < crystalSkillIds.size(); i++) {
			String[] tempString = crystalSkillIds.get(i).split("}");
			for(int j = 0; j < tempString.length; j++) {
				String[] tempString2 = tempString[j].split("=");
				if(!crystalSkillIdList.contains(tempString2[2].replaceAll(" ", ""))) {
					crystalSkillIdList.add(tempString2[2].replaceAll(" ", ""));
				}
			}
		}
		
		for(int i = 0; i < crystalSkillIdList.size(); i++) {
			XmlController crystalSkills = new XmlController(basePath + srcVersion + skills2Xml, basePath + dstVersion + skills2Xml, skills2WorkSheet, crystalSkillIdList.get(i), "80000", "90000", specialIndexBySkills2, null);
			crystalSkills.copyMultiRowsByCrystalSkills();
			
			for(int j = 0; j < crystalSkills.getReturnContentByIndex().size(); j++) {
				if(!crystalEffectsReturnContentList.contains(crystalSkills.getReturnContentByIndex().get(j))) {
					crystalEffectsReturnContentList.add(crystalSkills.getReturnContentByIndex().get(j));
				}
			}
		}
		
		
		ArrayList<String> crystalEffectsIdList = new ArrayList<>();
		for(int i = 0; i < crystalEffectsReturnContentList.size(); i++) {
			String[] tempStringList = crystalEffectsReturnContentList.get(i).replaceAll(" ", "").split(",");
			for(int j = 0; j < tempStringList.length; j++) {
				if(!crystalEffectsIdList.contains(tempStringList[j])) {
					crystalEffectsIdList.add(tempStringList[j]);
				}
			}
		}
		for(int i = 0; i < crystalEffectsIdList.size(); i++) {
			XmlController crystalEffects = new XmlController(basePath + srcVersion + effectXml, basePath + dstVersion + effectXml, effectWorkSheet, crystalEffectsIdList.get(i), "80000", "90000", null, null);
			crystalEffects.copyMultiRowsBySkills();

			//System.out.println("crystal formula is " + crystalEffectsIdList.get(i));
			XmlController crystalFormulas = new XmlController(basePath + srcVersion + formulasXml, basePath + dstVersion + formulasXml, formulasWorkSheet, crystalEffectsIdList.get(i), "80000", "90000", null, null);
			crystalFormulas.CopyMultiRowsByFormulas();
		}

		ArrayList<String> ringSkillIds = new ArrayList<>();
		for(int i = 0; i < ringSkillGroup.size(); i++) {
			XmlController ringSkillGroupXC = new XmlController(basePath + srcVersion + skillGroupXml, basePath + dstVersion + skillGroupXml, skillGroupWorkSheet, ringSkillGroup.get(i), "600000", "700000", specialIndexBySkillGroup, null);
			ringSkillGroupXC.copyMultiRowsByCrystalSkillGroup();
			
			if(!srcVersion.equals("0_5")) {
				String srcSkillGroupLanguageXml = "\\data\\language_packs\\" + srcLanguage + "\\skill_xml_skillgroup.xml";
				String dstSkillGroupLanguageXml = "\\data\\language_packs\\" + dstLanguage + "\\skill_xml_skillgroup.xml";
				XmlController ringSkillGroupLanguage = new XmlController(basePath + srcVersion + srcSkillGroupLanguageXml, basePath + dstVersion + dstSkillGroupLanguageXml, skillGroupWorkSheet, ringSkillGroup.get(i), "600000", "700000", null, null);
				ringSkillGroupLanguage.copyRow();
			}
			
			for(int j = 0; j < ringSkillGroupXC.getReturnContentByIndex().size(); j++) {
				if(!ringSkillIds.contains(ringSkillGroupXC.getReturnContentByIndex().get(j))) {
					ringSkillIds.add(ringSkillGroupXC.getReturnContentByIndex().get(j));
				}
			}
		}		
		
		ArrayList<String> ringEffectsReturnContentList = new ArrayList<>();
		ArrayList<String> ringSkillIdList = new ArrayList<>();
		for(int i = 0; i < ringSkillIds.size(); i++) {
			String[] tempString = ringSkillIds.get(i).split("}");
			for(int j = 0; j < tempString.length; j++) {
				String[] tempString2 = tempString[j].split("=");
				if(!ringSkillIdList.contains(tempString2[2].replaceAll(" ", ""))) {
					ringSkillIdList.add(tempString2[2].replaceAll(" ", ""));
				}
			}
		}
		for(int i = 0; i < ringSkillIdList.size(); i++) {
			XmlController ringSkills = new XmlController(basePath + srcVersion + skills2Xml, basePath + dstVersion + skills2Xml, skills2WorkSheet, ringSkillIdList.get(i), "600000", "700000", specialIndexBySkills2, null);
			ringSkills.copyMultiRowsByCrystalSkills();
			
			for(int j = 0; j < ringSkills.getReturnContentByIndex().size(); j++) {
				if(!ringEffectsReturnContentList.contains(ringSkills.getReturnContentByIndex().get(j))) {
					ringEffectsReturnContentList.add(ringSkills.getReturnContentByIndex().get(j));
				}
			}
		}
		
		
		ArrayList<String> ringEffectsIdList = new ArrayList<>();
		for(int i = 0; i < ringEffectsReturnContentList.size(); i++) {
			String[] tempStringList = ringEffectsReturnContentList.get(i).replaceAll(" ", "").split(",");
			for(int j = 0; j < tempStringList.length; j++) {
				if(!ringEffectsIdList.contains(tempStringList[j])) {
					ringEffectsIdList.add(tempStringList[j]);
				}
			}
		}
		
		for(int i = 0; i < ringEffectsIdList.size(); i++) {
			XmlController ringEffects = new XmlController(basePath + srcVersion + effectXml, basePath + dstVersion + effectXml, effectWorkSheet, ringEffectsIdList.get(i), "600000", "700000", null, null);
			ringEffects.copyMultiRowsBySkills();

			//System.out.println("crystal formula is " + ringEffectsIdList.get(i));
			
			XmlController ringFormulas = new XmlController(basePath + srcVersion + formulasXml, basePath + dstVersion + formulasXml, formulasWorkSheet, ringEffectsIdList.get(i), "600000", "700000", null, null);
			ringFormulas.CopyMultiRowsByFormulas();
		}
		
		
		String skillGroupComboXml = "\\data\\combat\\skill_xml\\group_combo.xml";
		String skillGroupComboWorkSheet = "组合技规则|group_combo";
		
		
		

		
		System.out.println("-------------------------------------------------------Success!--------------------------------------------------------");
		return true;
	}
}