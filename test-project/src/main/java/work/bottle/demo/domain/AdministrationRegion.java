package work.bottle.demo.domain;

import java.util.Objects;

public class AdministrationRegion {

    private Integer id;
    private Integer pid;
    private Integer deep;
    private String name;
    private String pinyinPrefix;
    private String pinyinFull;
    private Long extId;
    private String extName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyinPrefix() {
        return pinyinPrefix;
    }

    public void setPinyinPrefix(String pinyinPrefix) {
        this.pinyinPrefix = pinyinPrefix;
    }

    public String getPinyinFull() {
        return pinyinFull;
    }

    public void setPinyinFull(String pinyinFull) {
        this.pinyinFull = pinyinFull;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    @Override
    public String toString() {
        return "AdministrativeRegion{" +
                "id=" + id +
                ", pid=" + pid +
                ", deep=" + deep +
                ", name='" + name + '\'' +
                ", pinyinPrefix='" + pinyinPrefix + '\'' +
                ", pinyinFull='" + pinyinFull + '\'' +
                ", extId=" + extId +
                ", extName='" + extName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdministrationRegion that = (AdministrationRegion) o;
        return Objects.equals(deep, that.deep) && Objects.equals(name, that.name) && Objects.equals(pinyinPrefix, that.pinyinPrefix) && Objects.equals(pinyinFull, that.pinyinFull) && Objects.equals(extId, that.extId) && Objects.equals(extName, that.extName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deep, name, pinyinPrefix, pinyinFull, extId, extName);
    }
}