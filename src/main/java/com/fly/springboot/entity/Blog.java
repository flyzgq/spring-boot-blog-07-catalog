package com.fly.springboot.entity;

import com.github.rjeschke.txtmark.Processor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author fly
 * @date 2018/5/20 23:24
 * @description     博客
 **/
@Entity
public class Blog implements  Serializable {

    private static final long serialVersionUID = -7238929067458659494L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长策略
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)//title不能为空
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min = 2, max = 300)
    @Column(nullable = false)
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)//懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String content;

    /**
     * 将md转为html
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)//懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String htmlContent;

    /**
     * 创建者
     */
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    /**
     * 访问量
     */
    @Column(name = "readSize")
    private Integer readSize = 0;

    /**
     * 评论量
     */
    @Column(name = "commentSize")
    private Integer commentSize = 0;

    /**
     * 点赞量
     */
    @Column(name = "voteSize")
    private Integer voteSize = 0;

    /**
     * 标签
     */
    @Column(name = "tags", length = 100)
    private String tags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
    protected Blog() {

    }

    public Blog(@NotEmpty(message = "标题不能为空") @Size(min = 2, max = 50) String title, @NotEmpty(message = "摘要不能为空") @Size(min = 2, max = 300) String summary, @NotEmpty(message = "内容不能为空") @Size(min = 2) String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public String getHtmlContent() {
        return htmlContent;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize = this.comments.size();
    }

    /**
     * 添加评论
     * @param comment
     */
    public void addComment(Comment comment){
        this.comments.add(comment);
        this.commentSize = this.comments.size();
    }

    /**
     * 删除评论
     * @param commentId
     */
    public void removeComment(Long commentId){
        for (int i = 0; i < this.comments.size(); i++) {
            if (comments.get(i).getId().equals(commentId) ){
                this.comments.remove(i);
                break;
            }
        }
        this.commentSize = this.comments.size();
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize = this.votes.size();
    }

    /**
     * 点赞
     * @param vote
     * @return
     */
    public boolean addvote(Vote vote){
        boolean isExist = false;
        for (int i = 0; i < this.votes.size(); i++) {
            if(this.votes.get(i).getUser().getId().equals(vote.getUser().getId())){
                isExist = true;
                break;
            }
        }
        if(!isExist){
            this.votes.add(vote);
            this.voteSize = this.votes.size();
        }
        return isExist;
    }

    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId){
        for (int i = 0; i < this.votes.size(); i++) {
            if(this.votes.get(i).getId().equals(voteId)){
                this.votes.remove(i);
                break;
            }
        }
        this.voteSize = this.votes.size();
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
