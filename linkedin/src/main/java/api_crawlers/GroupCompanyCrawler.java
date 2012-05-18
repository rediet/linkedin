package api_crawlers;

import java.util.List;

import model.Request;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInCompany;
import structure.LInGroup;

public class GroupCompanyCrawler extends Crawler {
	
	public static final String COMPANY_FIELDS = "(id,name,universal-name,num-followers)";
	public static final String GROUP_FIELDS = "(group:(id,name,num-members,location:(country,postal-code)))";


	public GroupCompanyCrawler(Request requester) {
		super(requester);
	}

	public List<LInGroup> getGroupMemberships() {
		Response response = requester.GET("people/~/group-memberships:"
				+ GROUP_FIELDS);
		Element element = Elements.fromResponse(response);
		return convertGroup(Elements.extract(element, ElementType.GROUP));
	}

	public List<LInCompany> getCompaniesFollowing() {
		Response response = requester.GET("people/~/following/companies:"
				+ COMPANY_FIELDS);
		Element element = Elements.fromResponse(response);
		return convertCompany(Elements.extract(element, ElementType.COMPANY));
	}

}
